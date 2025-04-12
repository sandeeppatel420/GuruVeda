package com.example.guruveda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruveda.DataModel.SubjectModel

class SelectedActivity : AppCompatActivity(){
    lateinit var myAdapter: SelectedAdapter
    lateinit var dataList:ArrayList<SubjectModel>
    lateinit var selectecRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)

        selectecRecyclerView=findViewById(R.id.selected_RecyclerView)
        selectecRecyclerView.layoutManager=LinearLayoutManager(this)

        dataList=ArrayList()
        dataList.add(SubjectModel("Kotlin"))
        dataList.add(SubjectModel("Java"))
        dataList.add(SubjectModel("Kotlin"))
        dataList.add(SubjectModel("Kotlin"))
        dataList.add(SubjectModel("Kotlin"))

        myAdapter=SelectedAdapter(this,dataList)
        selectecRecyclerView.adapter=myAdapter
    }

//    override fun onChatListItemClick(position: Int) {
//        val fragment = CourseFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout_container, fragment) // Make sure this FrameLayout exists in activity_selected.xml
//            .addToBackStack(null)
//            .commit()
//    }
}