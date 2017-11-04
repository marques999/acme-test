package org.marques999.acme.printer.common

import android.app.Activity
import android.app.AlertDialog

import org.marques999.acme.printer.R

import android.content.Context
import android.content.DialogInterface

object AcmeDialogs {

    /**
     */
    fun buildOk(
        context: Context,
        resourceId: Int,
        vararg format: Any
    ): AlertDialog.Builder = AlertDialog
        .Builder(context)
        .setTitle(R.string.app_name)
        .setMessage(context.getString(resourceId, *format))
        .setPositiveButton(android.R.string.ok, null)

    /**
     */
    fun buildOk(
        context: Context,
        title: String,
        message: String
    ): AlertDialog.Builder = AlertDialog
        .Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)

    /**
     */
    fun buildYesNo(
        context: Activity,
        resourceId: Int,
        callback: DialogInterface.OnClickListener
    ): AlertDialog.Builder = AlertDialog
        .Builder(context)
        .setTitle(R.string.app_name)
        .setMessage(resourceId)
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, callback)
}