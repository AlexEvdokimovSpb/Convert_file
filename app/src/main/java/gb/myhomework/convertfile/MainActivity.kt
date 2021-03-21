package gb.myhomework.convertfile

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import gb.myhomework.convertfile.databinding.ActivityMainBinding
import gb.myhomework.convertfile.mvp.model.Converter
import gb.myhomework.convertfile.mvp.model.Image
import gb.myhomework.convertfile.mvp.presenter.MainPresenter
import gb.myhomework.convertfile.mvp.view.MainView
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter


class MainActivity : MvpAppCompatActivity(), MainView {

    private val vb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val presenter by moxyPresenter {
        MainPresenter(Converter(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        vb.btnConvert.setOnClickListener { presenter.forConvertToClick() }
    }

    override fun pickImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST_ID
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let { uri ->
                    val bytes = this?.contentResolver?.openInputStream(uri)?.buffered()?.use {
                        it.readBytes()
                    }
                    bytes?.let {
                        presenter.imageSelected(Image(bytes))
                    }
                }
            }
        }
    }

    var convertInProgressDialog: Dialog? = null
    override fun showConvertInProgress() {
        this?.let {
            convertInProgressDialog = AlertDialog.Builder(it).setMessage(getString(R.string.converting))
                .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                    presenter.convertCancel()
                }
                .create()
            convertInProgressDialog?.show()
        }
    }

    override fun hideConverterInProgress() {
        convertInProgressDialog?.dismiss()
    }

    override fun showConverterSuccess() {
        Toast.makeText(this, getString(R.string.successful), Toast.LENGTH_SHORT).show();
    }

    override fun showConvertError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
    }

    override fun showConverterCancel() {
        Toast.makeText(this, getString(R.string.caceled), Toast.LENGTH_SHORT).show();
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_ID = 42
    }
}