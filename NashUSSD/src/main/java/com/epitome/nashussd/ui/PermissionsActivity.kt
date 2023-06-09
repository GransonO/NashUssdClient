package com.epitome.nashussd.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.epitome.nashussd.R
import com.epitome.nashussd.databinding.ActivityPermissionsBinding
import com.epitome.nashussd.utils.AccessibilityHelper
import com.epitome.nashussd.utils.AccessibilityHelper.isAccessibilityServiceEnabled
import com.epitome.nashussd.utils.AccessibilityHelper.isPhonePermissionGranted
import com.epitome.nashussd.utils.AccessibilityHelper.isPhoneStatePermissionGranted
import com.epitome.nashussd.utils.enums.PermissionEnums

class PermissionsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPermissionsBinding.inflate(layoutInflater) }
    private var permissionType = PermissionEnums.PHONE.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        permissionType = intent.getStringExtra("permission").toString()

        with(binding){
            when(permissionType){
                PermissionEnums.PHONE.toString() -> {
                    button.text = getString(R.string.enable_phone)
                    header.text = getString(R.string.allow_nash_phone)
                }
                PermissionEnums.SERVICE.toString() -> {
                    button.text= getString(R.string.enable_service)
                    header.text = getString(R.string.allow_nash_service)
                }
                PermissionEnums.STATE.toString() -> {
                    button.text= getString(R.string.allow_phone_state)
                    header.text = getString(R.string.allow_phone_state_txt)
                }
            }

            button.setOnClickListener {
                when(permissionType){
                    PermissionEnums.PHONE.toString() -> {
                        AccessibilityHelper.requestPhonePermissions(this@PermissionsActivity)
                    }

                    PermissionEnums.SERVICE.toString() -> {
                        AccessibilityHelper.openAccessibilitySettings(this@PermissionsActivity)
                    }

                    PermissionEnums.STATE.toString() -> {
                        AccessibilityHelper.requestPhoneStatePermissions(this@PermissionsActivity)
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        when{
            (isPhonePermissionGranted(this) && permissionType == PermissionEnums.PHONE.toString()) -> {
                finish()
            }
            (isPhoneStatePermissionGranted(this) && permissionType == PermissionEnums.STATE.toString()) -> {
                finish()
            }
            (isAccessibilityServiceEnabled(this) && permissionType == PermissionEnums.SERVICE.toString())  -> {
                finish()
            }
        }

    }
}