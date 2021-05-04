package com.jks.ketchup.adapters

import android.content.Context
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.entity.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_main_layout.view.*
import javax.inject.Inject

class AllUsersAdapter @Inject constructor(val context: Context) : PagingDataAdapter<User,AllUsersAdapter.AllUserViewHolder>(diffCallback){


    @Inject
    lateinit var glide : RequestManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_main_layout,parent,false)
        return AllUserViewHolder(view)

    }
    override fun onBindViewHolder(holder: AllUserViewHolder, position: Int) {

        val userss = getItem(position) ?: return

        val name = userss.userName!!
        val touid=userss.uid!!


        holder.apply {

            Glide.with(context).load(userss.profilepicurl).into(profileimage)

            username.text=userss.userName
            statuss.text=userss.status


            if(userss.status=="online"){
                holder.itemView.iv_onlineicon.isVisible= true
            }else{
                holder.itemView.iv_onlineicon.isVisible= false
            }

        }

        holder.addtoFtrind.isVisible=true
        holder.addtoFtrind.setOnClickListener {

            val firestore = FirebaseFirestore.getInstance().collection("contacts")
            val uid = FirebaseAuth.getInstance().uid!!

            val ref = firestore.document(uid).collection("userContacts").document(touid)
            val newuser = User(
                userName = name,
                uid = touid,
                gender = userss.gender,
                status = userss.status,
                profilepicurl = userss.profilepicurl,
                age = userss.age
            )


            val tasks = firestore.document(uid).collection("userContacts").document(touid).get().addOnCompleteListener {



                if(it.isSuccessful){
                    if(it.result!!.exists()){
                        Toast.makeText(context, "room already exists", Toast.LENGTH_LONG).show()
                    }else{


                        val f= firestore.document(uid).collection("userContacts").get().addOnCompleteListener {
                            var count=0;
                            for(d in it.result!!){
                                count++;
                            }
                            if(count>=10){
                                Toast.makeText(context, "you can add maximum 10 friends", Toast.LENGTH_LONG).show()
                            }else{

                                if(FirebaseAuth.getInstance().uid!! == userss.uid){
                                    Toast.makeText(context, "you cant be a friend of yourself", Toast.LENGTH_LONG).show()
                                }else{
                                    ref.set(newuser).addOnCompleteListener {
                                        Toast.makeText(context, "added", Toast.LENGTH_LONG).show()
                                    }
                                }


                            }
                        }



                    }
                }
            }




        }

    }
    object diffCallback : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: User, newItem:User): Boolean {
            return oldItem.uid == newItem.uid
        }
    }
    class AllUserViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val profileimage:CircleImageView = view.findViewById(R.id.user_main_profile)
        val username: TextView = view.findViewById(R.id.tv_main_username)
        val statuss: TextView = view.findViewById(R.id.tv_time)
        val addtoFtrind:TextView=view.findViewById(R.id.tv_main_addfriend)
    }
}