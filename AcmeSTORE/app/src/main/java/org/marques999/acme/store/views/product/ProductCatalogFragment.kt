package org.marques999.acme.store.views.product

import android.os.Bundle
import android.app.ProgressDialog
import android.support.v7.widget.LinearLayoutManager

import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

import android.content.Intent
import android.content.Context

import org.marques999.acme.store.R
import org.marques999.acme.store.AcmeDialogs
import org.marques999.acme.store.AcmeStore

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater

import org.marques999.acme.store.model.Product
import org.marques999.acme.store.api.HttpErrorHandler
import org.marques999.acme.store.views.BottomNavigationAdapter
import org.marques999.acme.store.views.main.MainActivityMessage
import org.marques999.acme.store.views.main.MainActivityFragment
import org.marques999.acme.store.views.main.MainActivityListener

import kotlinx.android.synthetic.main.fragment_catalog.*

class ProductCatalogFragment : MainActivityFragment(R.layout.fragment_catalog), ProductCatalogListener {

    /**
     */
    private lateinit var products: ArrayList<Product>
    private lateinit var progressDialog: ProgressDialog

    /**
     */
    private var mainActivityListener: MainActivityListener? = null

    /**
     */
    override fun onItemPurchased(product: Product) {
        mainActivityListener?.onNotify(MainActivityMessage.PURCHASE, product)
    }

    /**
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(BUNDLE_PRODUCTS, products)
    }

    /**
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = AcmeDialogs.buildProgress(context, R.string.global_loading)
    }

    /**
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LayoutInflater.from(context).inflate(
        R.layout.fragment_catalog, container, false
    )

    /**
     */
    override fun onItemSelected(product: Product) = startActivity(Intent(
        activity, ProductViewActivity::class.java
    ).putExtra(
        ProductViewActivity.EXTRA_PRODUCT, product
    ))

    /**
     */
    private fun initializeRecycler(products: List<Product>) = catalog_recyclerView.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        clearOnScrollListeners()
        adapter = ProductCatalogAdapter(products, this@ProductCatalogFragment)
    }

    /**
     */
    override fun onRefresh() {

        progressDialog.show()

        (activity.application as AcmeStore).api.getProducts().observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(
            Schedulers.io()
        ).subscribe({
            progressDialog.dismiss()
            products = ArrayList(it)
            initializeRecycler(it)
            mainActivityListener?.onUpdateBadge(BottomNavigationAdapter.CATALOG, it.size)
        }, {
            progressDialog.dismiss()
            HttpErrorHandler(context).accept(it)
        })
    }

    /**
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            onRefresh()
        } else {
            products = savedInstanceState.getParcelableArrayList(BUNDLE_PRODUCTS)
            mainActivityListener?.onUpdateBadge(BottomNavigationAdapter.CATALOG, products.size)
            initializeRecycler(products)
        }
    }

    /**
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivityListener = context as? MainActivityListener
    }

    /**
     */
    override fun onDetach() {
        super.onDetach()
        mainActivityListener = null
    }

    /**
     */
    companion object {
        private val BUNDLE_PRODUCTS = "org.marques999.acme.bundles.PRODUCTS"
    }
}