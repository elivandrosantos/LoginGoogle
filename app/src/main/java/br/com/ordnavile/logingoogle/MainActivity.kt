package br.com.ordnavile.logingoogle

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Properties
import javax.mail.Message.RecipientType
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var signedInAccount by remember { mutableStateOf<GoogleSignInAccount?>(null) }
            val context = this

            Column(modifier = Modifier.padding(16.dp)) {
                GoogleSignInButton(context) { account ->
                    signedInAccount = account
                }

                signedInAccount?.let { account ->
                    Text(
                        text = "Signed in as: ${account.email}",
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Button(
                        onClick = {
                            lifecycleScope.launch {
                                try {
                                    sendEmail(
                                        context,
                                        account,
                                        "afrodocs@gmail.com",
                                        "Testando",
                                        "Fazendo teste de aplicativo."
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Log.e("EmailError", "Error sending email", e)
                                }

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Send Email")
                    }
                }
            }

        }
    }


}

@Composable
fun GoogleSignInButton(context: Context, onSignedIn: (GoogleSignInAccount) -> Unit) {

    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task, onSignedIn)
        }
    val credentialManager = CredentialManager.create(context)
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(context.getString(R.string.web_id))
        .requestScopes(Scope("https://www.googleapis.com/auth/gmail.send"))
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    Button(
        onClick = {
            scope.launch {
                credentialManager.clearCredentialState(request = ClearCredentialStateRequest())
            }
            launcher.launch(googleSignInClient.signInIntent)

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("Sign in with Google")
    }
}

private fun handleSignInResult(
    completedTask: Task<GoogleSignInAccount>,
    onSignedIn: (GoogleSignInAccount) -> Unit
) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        onSignedIn(account)
    } catch (e: ApiException) {
        e.printStackTrace()
    }
}

suspend fun sendEmail(
    context: Context,
    account: GoogleSignInAccount,
    recipient: String,
    subject: String,
    body: String
) {
    withContext(Dispatchers.IO) {
        try {
            val credential = GoogleAccountCredential.usingOAuth2(
                context, listOf("https://www.googleapis.com/auth/gmail.send")
            ).setSelectedAccount(account.account)


            val gmail =
                Gmail.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName(context.getString(R.string.app_name))
                .build()


            val emailContent = createEmail(recipient, account.email!!, subject, body)
            sendMessage(gmail, "me", emailContent)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("EmailError", "Error initializing Gmail service", e)
        }

    }
}

@Throws(MessagingException::class, IOException::class)
private fun createEmail(to: String, from: String, subject: String, bodyText: String): MimeMessage {
    val props = Properties()
    val session = Session.getDefaultInstance(props, null)
    val email = MimeMessage(session)

    email.setFrom(InternetAddress(from))
    email.addRecipient(RecipientType.TO, InternetAddress(to))
    email.subject = subject
    email.setText(bodyText)

    return email
}

@Throws(IOException::class, MessagingException::class)
private fun sendMessage(service: Gmail, userId: String, email: MimeMessage) {
    val buffer = ByteArrayOutputStream()
    email.writeTo(buffer)
    val bytes = buffer.toByteArray()
    val encodedEmail = Base64.encodeToString(bytes, Base64.URL_SAFE)
    val message = Message().setRaw(encodedEmail)
    service.users().messages().send(userId, message).execute()
}