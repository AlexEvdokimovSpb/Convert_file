package gb.myhomework.convertfile.mvp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class Converter(private val context: Context?) : IConverter {
    override fun convert(image: Image) = Completable.fromAction {
        context?.let {
            val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
            val filename = File(context.getExternalFilesDir(null), "converted.png")
            val stream = FileOutputStream(filename)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        }
    }.subscribeOn(Schedulers.io())
}