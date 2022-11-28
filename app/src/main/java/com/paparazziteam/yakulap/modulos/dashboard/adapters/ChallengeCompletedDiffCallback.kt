package com.paparazziteam.yakulap.modulos.dashboard.adapters

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.paparazziteam.yakulap.modulos.dashboard.pojo.ChallengeCompleted

class ChallengeCompletedDiffCallback(private val oldList: List<ChallengeCompleted>,private val newList: List<ChallengeCompleted>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id == newList[newItemPosition].id -> true
            oldList[oldItemPosition].challenge_id == newList[newItemPosition].challenge_id -> true
            oldList[oldItemPosition].author_name == newList[newItemPosition].author_name -> true
            oldList[oldItemPosition].post_blocked == newList[newItemPosition].post_blocked -> true
            oldList[oldItemPosition].tipo == newList[newItemPosition].tipo -> true
            else -> false
        }
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}