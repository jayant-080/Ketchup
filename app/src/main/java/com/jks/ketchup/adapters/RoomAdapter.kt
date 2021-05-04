package com.jks.ketchup.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jks.ketchup.R
import com.jks.ketchup.entity.Room
import com.jks.ketchup.ui.MainActivity
import com.jks.ketchup.ui.setup.fragments.ProfilePicFragmentDirections
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomAdapter @Inject constructor(val context: Context) : PagingDataAdapter<Room,RoomAdapter.RoomViewHolder>(diffCallback){


    @Inject
    lateinit var glide : RequestManager
    var roomType= ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_room_layout,parent,false)
        return RoomViewHolder(view)

    }
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {

        val rooms = getItem(position) ?: return
        holder.apply {

            Glide.with(context).load(rooms.picurl).into(roomPic)
            roomName.text = rooms.roomName
            roomDesc.text = rooms.roomDesc.toString()
            roomCode.text=rooms.roomType
            val count=rooms.joinList.size
            roomjoinCount.text="$count"
            if(rooms.roomType=="private"){
                ivlock.isVisible = true
            }
            if(rooms.roomType=="public"){
                ivunlock.isVisible= true
            }
        }
        holder.itemView.setOnClickListener {

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("code",rooms.roomCode)
            intent.putExtra("url",rooms.picurl)
            intent.putExtra("name",rooms.roomName)
            intent.putExtra("roomType",rooms.roomType)
            intent.putExtra("roomurl",rooms.uid)
            context.startActivity(intent)

        }

    }
    object diffCallback : DiffUtil.ItemCallback<Room>() {
        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.uid == newItem.uid
        }
    }
    class RoomViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview)
    {
        val roomPic : CircleImageView =itemview.findViewById(R.id.iv_room_image)
        val roomName : TextView= itemview.findViewById(R.id.tv_room_layout_name)
        val roomDesc : TextView= itemview.findViewById(R.id.tv_room_layout_desc)
        val roomCode : TextView= itemview.findViewById(R.id.room_layout_code)
        val ivlock : ImageView = itemview.findViewById(R.id.iv_lock)
        val ivunlock : ImageView = itemview.findViewById(R.id.iv_unlock)
        val roomjoinCount : TextView= itemview.findViewById(R.id.tv_room_layout_joincount)
    }
}