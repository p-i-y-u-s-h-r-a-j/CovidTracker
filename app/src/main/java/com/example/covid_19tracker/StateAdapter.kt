package com.example.covid_19tracker

import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class StateAdapter(val list: List<StatewiseItem>) : BaseAdapter(){
    override fun getCount(): Int = list.size

    override fun getItem(p0: Int) = list[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, converterView: View?, parent: ViewGroup?): View {
        val view = converterView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_list,parent,false)
        val item = list[position]
        view.CNFMTv.text = SpannableDelta("${item.confirmed}\n ⇪${item.deltaconfirmed ?:"0"}",
        "#D32F2F",
            item.confirmed?.length?: 0
            )
        view.ACTVTv.text = SpannableDelta("${item.active}\n ⇪${item.deltaactive ?:"0"}",
            "#1976D2",
            item.confirmed?.length?: 0
        )
        view.RCVDTv.text = SpannableDelta("${item.recovered}\n ⇪${item.deltarecovered ?:"0"}",
            "#388E3C",
            item.recovered?.length?: 0
        )
        view.DTHTv.text = SpannableDelta("${item.deaths}\n ⇪${item.deltadeaths ?:"0"}",
            "#FBC02D",
            item.deaths?.length?: 0
        )
        view.stateTv.text = item.state
        return view
    }

}