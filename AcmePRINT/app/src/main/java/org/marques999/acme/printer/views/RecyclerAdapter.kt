package org.marques999.acme.printer.views

import android.view.ViewGroup

import org.marques999.acme.printer.orders.Order
import org.marques999.acme.printer.orders.OrderAdapter
import org.marques999.acme.printer.customers.CreditCardAdapter
import org.marques999.acme.printer.customers.CustomerAdapter
import org.marques999.acme.printer.products.ProductAdapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView

class RecyclerAdapter(private val order: Order) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     */
    private val items = arrayListOf<ViewType>(
        order.customer,
        order.customer.credit_card,
        order,
        *order.products
    )

    /**
     */
    private val delegateAdapters = SparseArrayCompat<ViewTypeAdapter>()

    /**
     */
    init {
        delegateAdapters.put(ViewType.ORDER, OrderAdapter())
        delegateAdapters.put(ViewType.PRODUCT, ProductAdapter())
        delegateAdapters.put(ViewType.CUSTOMER, CustomerAdapter())
        delegateAdapters.put(ViewType.CREDIT_CARD, CreditCardAdapter())
        notifyItemRangeChanged(0, items.size)
    }

    /**
     */
    fun getState(): Order = order

    /**
     */
    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int) = items[position].getViewType()

    /**
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    /**
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }
}