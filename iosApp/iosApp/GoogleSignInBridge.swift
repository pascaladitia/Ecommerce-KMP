import UIKit
import FirebaseCore
import GoogleSignIn
import ComposeApp

private func topViewController(
    base: UIViewController? = UIApplication.shared.connectedScenes
    .compactMap { ($0 as? UIWindowScene)?.keyWindow }
    .first?.rootViewController
) -> UIViewController? {
    if let nav = base as? UINavigationController { return topViewController(base: nav.visibleViewController) }
    if let tab = base as? UITabBarController { return topViewController(base: tab.selectedViewController) }
    if let presented = base?.presentedViewController { return topViewController(base: presented) }
    return base
}

func setupGoogleSignInBridge(webClientId: String? = nil) {
    KMPBridge.shared.registerGoogleLauncher(launcher: {
        DispatchQueue.main.async {
            guard let presenter = topViewController(),
                  let clientID = FirebaseApp.app()?.options.clientID else {
                KMPBridge.shared.onGoogleTokensReceived(idToken: nil, accessToken: nil)
                return
            }

            if let web = webClientId, !web.isEmpty {
                GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientID, serverClientID: web)
            } else {
                GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientID)
            }

            GIDSignIn.sharedInstance.signIn(withPresenting: presenter) { result, error in
                let user = result?.user
                let idToken: String? = user?.idToken?.tokenString
                let accessToken: String? = user?.accessToken.tokenString
                KMPBridge.shared.onGoogleTokensReceived(idToken: idToken, accessToken: accessToken)
            }
        }
    })
}
