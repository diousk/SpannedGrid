package com.diousk.spannedgrid

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.diousk.spannedgrid.SpannedGridLayoutManager2.GridSpanLookup
import com.diousk.spannedgrid.SpannedGridLayoutManager2.SpanInfo
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = SpannedGridLayoutManager2(
            GridSpanLookup { position ->
                if (position % 7 == 0) {
                    SpanInfo(2, 2)
                } else {
                    SpanInfo(1, 1)
                }
            },
            5,  // number of columns
            (screenWidthInPx() / 5) / 70.dpToPx().toFloat() // how big is default item
        )
        val adapter = RvAdapter()

        val mgr = SpannedGridLayoutManager(
            orientation = SpannedGridLayoutManager.Orientation.VERTICAL,
            spans = 5)
        mgr.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
            if (position % 7 == 0) {
                SpanSize(2, 2)
            } else {
                SpanSize(1, 2)
            }
        }

        rvItems.layoutManager = manager
        rvItems.adapter = adapter

//        adapter.setData(listOf("0"))
        adapter.setData((0..4).map { it.toString() })
//        lifecycleScope.launch {
//            delay(3000)
//            adapter.setData((0..1).map { it.toString() })
//            delay(3000)
//            adapter.setData((0..69).map { it.toString() })
//            delay(3000)
//            adapter.setData(listOf("0"))
//        }
    }
}

fun Context?.screenWidthInPx(): Int {
    if (this == null) return 0
    val dm = DisplayMetrics()
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

fun Number.dpToPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

class RvAdapter: RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0) {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout_large, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun getItemCount(): Int = items.size

    fun setData(data: List<String>) {
        Log.d("VIEW", "data $data")
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.textView)
        fun bind(str: String) {
            Log.d("VIEW", "str $str")
            textView.text = str
        }
    }
}