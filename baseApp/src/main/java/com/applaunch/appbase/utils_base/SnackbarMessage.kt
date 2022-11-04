

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.applaunch.appbase.utils_base.CodeSnippet

object SnackbarMessage {
    fun showNetworkMessage(view: View, codeSnippet: CodeSnippet) {
        val snackBar = Snackbar.make(view, "No Network found!", Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.RED)
        snackBar.setTextColor(Color.WHITE)
        snackBar.setAction("Settings") { codeSnippet.showNetworkSettings() }
        snackBar.show()
    }

}