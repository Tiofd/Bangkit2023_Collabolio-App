package com.bangkit.collabolio.ui.userprofile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.collabolio.R
import com.bangkit.collabolio.databinding.FragmentUserBinding
import com.bangkit.collabolio.response.UserProfileResponse
import com.bangkit.collabolio.ui.InputBioActivity
import com.bangkit.collabolio.ui.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.userProfileData.observe(viewLifecycleOwner) { userProfile ->
            displayUserProfile(userProfile)
        }
        userViewModel.getUserProfile()
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserProfile(userProfile: UserProfileResponse?) {
        if (userProfile != null) {
            binding.tvEmail.text = resources.getString(R.string.profile_email, userProfile.email)
            binding.tvName.text = resources.getString(R.string.profile_name, userProfile.profile.displayName)
            binding.tvPhoneNumber.text = resources.getString(R.string.profile_number_phone, userProfile.profile.phoneNumber)
            binding.tvBirthDate.text = resources.getString(R.string.profile_birthdate, userProfile.profile.birthDate)
            binding.tvLocation.text = resources.getString(R.string.profile_location, userProfile.profile.location)
            binding.tvBio.text = resources.getString(R.string.profile_bio, userProfile.profile.bio)
            binding.tvEducation.text = resources.getString(R.string.profile_education, "${userProfile.profile.educations.degree} - ${userProfile.profile.educations.school}")
            val genderText = if (userProfile.profile.male != null) {
                if (userProfile.profile.male) {
                    "Laki-Laki"
                } else {
                    "Perempuan"
                }
            } else {
                "tidak diketahui"
            }
            binding.tvIsMale.text = resources.getString(R.string.profile_gender, genderText)
            Glide.with(requireContext())
                .load(userProfile.profile.photoURL)
                .into(binding.userImageView)

            binding.btnEditProfile.setOnClickListener{
                val intent = Intent(requireContext(), InputBioActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogout.setOnClickListener {
                signOut()
            }


        }
    }
    private fun signOut() {
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}