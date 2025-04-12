package com.example.guruveda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.guruveda.DataModel.SubjectModel

class SelectedAdapter(val myContext: Context,val user:ArrayList<SubjectModel> ):RecyclerView.Adapter<SelectedAdapter.SelectedViewHolder> (){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedViewHolder {
        val myView=LayoutInflater.from(myContext).inflate(R.layout.selected_item,parent,false)
        return SelectedViewHolder(myView)
    }

    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
       val users=user[position]
        holder.subText.text=users.subjectName

        holder.itemView.setOnClickListener {
          val intent=Intent(myContext,MainActivity::class.java)
            intent.putExtra("Courses","Courses")
            myContext.startActivity(intent)
            if (myContext is Activity) {
                myContext.finish()
            }

        }
    }

    override fun getItemCount(): Int {
       return user.size
    }

    class SelectedViewHolder(itemView: View):ViewHolder(itemView){
       val subText=itemView.findViewById<TextView>(R.id.subject_text)
    }
}