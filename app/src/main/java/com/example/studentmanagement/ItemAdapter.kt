package com.example.studentmanagement

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ItemAdapter(val items: Array<Student>): RecyclerView.Adapter<ItemViewHolder>() {
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return ItemViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.studentName.text = items[position].name
        holder.studentId.text = items[position]._id.toString()
        holder.email.text = createEmail(items[position].name, items[position]._id.toString())
    }

    fun createEmail(name: String, studentId: String): String {
        val fullName = name.split(" ")
        val firstName = fullName[0].toLowerCase(Locale.ROOT)[0]
        val middleName = if (fullName.size > 1) fullName.subList(1, fullName.size - 1).joinToString("").toLowerCase(Locale.ROOT)[0] else ""
        val lastName = fullName.last().toLowerCase(Locale.ROOT)
        return "$lastName.$firstName$middleName$studentId@sis.hust.edu.vn"
    }
}

class ItemViewHolder(val itemView: View, listener: ItemAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView) {
    val studentName: TextView
    val studentId: TextView
    val email: TextView

    init {
        studentName = itemView.findViewById(R.id.student_name)
        studentId = itemView.findViewById(R.id.student_id)
        email = itemView.findViewById(R.id.email)

        itemView.setOnClickListener {
            listener.onClick(adapterPosition)
        }
    }
}