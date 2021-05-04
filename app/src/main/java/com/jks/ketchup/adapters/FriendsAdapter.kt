package com.jks.ketchup.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jks.ketchup.R
import com.jks.ketchup.entity.User
import com.jks.ketchup.ui.chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_main_layout.view.*
import javax.inject.Inject


class FriendsAdapter @Inject constructor(var userlist: List<User>, val context: Context, val toUser:ArrayList<User>, val fromUser:User) : PagingDataAdapter<User,FriendsAdapter.UserViewHolder>(diffCallback) {

   @Inject
   lateinit var glide : RequestManager



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
         val views = LayoutInflater.from(context).inflate(R.layout.user_main_layout,parent,false)
        return UserViewHolder(views)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {



        val userss = userlist[position]


        val name = userss.userName!!
        val touid=userss.uid!!



          holder.apply {

              Glide.with(context).load(userss.profilepicurl).into(profileimage)
              username.text=name
              statuss.isVisible=false

//              if(userss.status=="online"){
//                   holder.view.iv_onlineicon.isVisible= true
//              }else{
//                  holder.view.iv_onlineicon.isVisible= false
//              }

          }








        holder.addtofriend.isVisible=false
        holder.itemView.setOnClickListener {

            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("fromUser", fromUser)
            intent.putExtra("toUser", toUser[position])
            intent.putExtra("roomId", "noRoomId")
            context.startActivity(intent)

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

    class UserViewHolder(val view : View) : RecyclerView.ViewHolder(view){
        val profileimage:CircleImageView = view.findViewById(R.id.user_main_profile)
        val username: TextView = view.findViewById(R.id.tv_main_username)
        val statuss: TextView = view.findViewById(R.id.tv_time)
        val addtofriend: TextView = view.findViewById(R.id.tv_main_addfriend)
    }
}