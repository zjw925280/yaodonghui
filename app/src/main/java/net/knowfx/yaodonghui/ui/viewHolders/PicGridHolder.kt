package net.knowfx.yaodonghui.ui.viewHolders

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import net.knowfx.yaodonghui.R
import net.knowfx.yaodonghui.databinding.LayoutItemPicBinding
import net.knowfx.yaodonghui.base.BaseListData
import net.knowfx.yaodonghui.entities.PicData
import net.knowfx.yaodonghui.ext.jumpFromPush
import net.knowfx.yaodonghui.ext.jumpToTarget
import net.knowfx.yaodonghui.ext.setOnclick
import net.knowfx.yaodonghui.ui.activity.ClassContentActivity

class PicGridHolder(view: ViewGroup, id: Int) : BaseViewHolder(view, id) {
    override fun onBind(
        data: BaseListData,
        position: Int,
        onItemClicked: ((view: View, data: BaseListData, position: Int) -> Unit)?
    ) {
        data as PicData
        val path = when {
            data.picLocalPath.isNotEmpty() -> {
                data.picLocalPath
            }
            data.picServicePath.isNotEmpty() -> {
                data.picServicePath
            }
            else -> {
                ""
            }
        }
        val viewBinding = LayoutItemPicBinding.bind(itemView)
        if (path.isEmpty()) {
            viewBinding.picIv.load(R.mipmap.icon_pic_add)
        } else {
            viewBinding.picIv.load(path) {
                transformations(RoundedCornersTransformation(4f))
            }
        }
        viewBinding.picIv.setOnClickListener {


        }
        itemView.setOnclick {
            onItemClicked?.invoke(it, data, position)

        }
    }
}