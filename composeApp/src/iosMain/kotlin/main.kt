import androidx.compose.ui.window.ComposeUIViewController
import org.pascal.ecommerce.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
