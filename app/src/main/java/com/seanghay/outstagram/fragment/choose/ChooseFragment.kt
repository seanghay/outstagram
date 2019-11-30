package com.seanghay.outstagram.fragment.choose

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seanghay.outstagram.OutstagramLoader
import com.seanghay.outstagram.R
import com.seanghay.outstagram.utility.AndroidUtility
import com.seanghay.outstagram.utility.PermissionUtility
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_choose.*
import kotlinx.android.synthetic.main.fragment_choose.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ChooseFragment : Fragment() {

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        // ignored
    }

    private val chooseAdapter = ChooseAdapter()
    private val viewModel: ChooseViewModel by viewModels()
    private val sharedPreferences =
        OutstagramLoader.appContext.getSharedPreferences("scroll", Context.MODE_PRIVATE)


    private fun safeStart() {
        recyclerView.addOnScrollListener(scrollListener)

        fabGoToTop.setOnClickListener {
            recyclerView.scrollToPosition(0)
        }

        viewModel.images.observe(viewLifecycleOwner, Observer {
            chooseAdapter.updateItems(it)
            recyclerView.post {
                val pos = sharedPreferences.getInt("pos", -1)
                if (pos != -1) {
                    (recyclerView?.layoutManager as? GridLayoutManager)?.scrollToPositionWithOffset(
                        pos,
                        0
                    )
                }
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            frameLayoutLoading.isGone = !it
            if (it) fabGoToTop.hide()
            else fabGoToTop.show()
        })

        if (chooseAdapter.items.isEmpty())
            viewModel.loadPhotos()

        chooseAdapter.itemClickDelegate = {
            viewModel.isLoading.value = true
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val file = Compressor(requireContext())
                        .setMaxWidth(1280)
                        .setMaxHeight(720)
                        .setQuality(70)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(
                            requireContext().externalCacheDir?.path
                                ?: requireContext().cacheDir.path
                        ).compressToFile(File(it.path))
                    withContext(Dispatchers.Main) {
                        findNavController()
                            .navigate(
                                R.id.action_chooseFragment_to_editorFragment,
                                bundleOf("path" to file.path)
                            )
                        viewModel.isLoading.value = false
                    }
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose, container, false).also {
            it.recyclerView.let { rv ->
                rv.layoutManager = createLayoutManger()
                rv.adapter = chooseAdapter
                rv.setHasFixedSize(true)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkPermissions()
    }

    private fun checkPermissions() {
        if (!PermissionUtility.isReadWriteGranted(this)) {
            if (PermissionUtility.isRationale(this, PermissionUtility.WRITE_EXTERNAL) ||
                PermissionUtility.isRationale(this, PermissionUtility.READ_EXTERNAL)
            ) {
                PermissionUtility.showPermissionRequiredDialog(this)
            } else {
                PermissionUtility.requestReadWrite(this)
            }
        } else safeStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AndroidUtility.RC_RATIONALE) {
            checkPermissions()
        }
    }

    override fun onDestroyView() {
        val grid = (recyclerView.layoutManager as? GridLayoutManager)
        val position = grid?.findFirstCompletelyVisibleItemPosition()

        sharedPreferences.edit()
            .putInt("pos", position ?: -1)
            .apply()

        recyclerView?.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AndroidUtility.RC_READ_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                safeStart()
                return
            }
        }
    }

    private fun createLayoutManger(): GridLayoutManager {
        return GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
    }

}