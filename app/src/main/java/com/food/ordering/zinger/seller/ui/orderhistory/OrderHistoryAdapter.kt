package com.food.ordering.zinger.seller.ui.orderhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.food.ordering.zinger.seller.R
import com.food.ordering.zinger.seller.data.model.OrderItemListModel
import com.food.ordering.zinger.seller.databinding.ItemPastOrderBinding
import java.lang.Exception
import java.text.SimpleDateFormat

class OrderHistoryAdapter(private val orderList: List<OrderItemListModel>, private val listener: OnItemClickListener) : RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {

        val binding: ItemPastOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_past_order,
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position], holder.adapterPosition, listener)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class OrderViewHolder(var binding: ItemPastOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderItemListModel, position: Int, listener: OnItemClickListener) {

            binding.textCustomerName.text = order.transactionModel.orderModel.userModel?.name
            try {
                val appDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm aaa")
                val dateString = appDateFormat.format(order.transactionModel.orderModel.date)
                binding.textOrderTime.text = dateString
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.textOrderId.text = order.transactionModel.orderModel.id.toString()

            binding.textOrderPrice.text =
                "₹ " + order.transactionModel.orderModel.price?.toInt().toString()
            var items = ""
            order.orderItemsList.forEach {
                items += it.quantity.toString() + " X " + it.itemModel.name + "\n"
            }

            binding.textOrderItems.text = items
            binding.textViewMore.visibility = if (order.orderItemsList.size > 2)  View.VISIBLE else View.GONE


            binding.textOrderStatus.text = order.orderStatusModel.last().orderStatus

            binding.layoutRoot.setOnClickListener { listener.onItemClick(order, position) }

        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: OrderItemListModel?, position: Int)

    }

}