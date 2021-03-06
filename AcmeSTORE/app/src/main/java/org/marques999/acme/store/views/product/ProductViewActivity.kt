package org.marques999.acme.store.views.product

import android.os.Bundle

import org.marques999.acme.store.R
import org.marques999.acme.store.AcmeStore
import org.marques999.acme.store.AcmeUtils

import com.squareup.picasso.Picasso

import org.marques999.acme.store.model.Product
import org.marques999.acme.store.views.BackButtonActivity

import kotlinx.android.synthetic.main.activity_product.*

class ProductViewActivity : BackButtonActivity() {

    /**
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        intent.getParcelableExtra<Product>(EXTRA_PRODUCT).let {

            (application as AcmeStore).shoppingCart[it.barcode]?.let {
                productView_purchase.text = getString(R.string.product_purchased)
            }

            productView_model.text = it.name
            productView_brand.text = it.brand
            productView_barcode.text = it.barcode
            productView_description.text = it.description
            productView_price.text = AcmeUtils.formatCurrency(it.price)

            Picasso.with(this).load(
                it.image_uri
            ).fit().centerInside().into(productView_photo)
        }
    }

    /**
     */
    companion object {
        val EXTRA_PRODUCT = "org.marques999.acme.extras.ORDER_PRODUCT"
    }
}