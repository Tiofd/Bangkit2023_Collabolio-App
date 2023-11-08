package com.bangkit.collabolio.ui

//noinspection SuspiciousImport
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import com.bangkit.collabolio.databinding.ActivityInputbioBinding
import com.bangkit.collabolio.response.Connections
import com.bangkit.collabolio.response.Education
import com.bangkit.collabolio.response.Interests
import com.bangkit.collabolio.response.Skills
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class InputBioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputbioBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputbioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        fetchSkills()
        fetchInterests()

        binding.btnInputBio.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val displayName = binding.inputName.text.toString()
        val phoneNumber = binding.inputNumber.text.toString()
        val birthDate = binding.inputDate.text.toString()
        val male = binding.radioButtonMale.isChecked
        val location = binding.inputAddress.text.toString()
        val degree = binding.inputDegree.text.toString()
        val school = binding.inputSchool.text.toString()
        val bio = binding.inputBio.text.toString()
        val userSkills = binding.mactvSkills.text.toString()
        val userInterests = binding.mactvInterests.text.toString()
        val arraySkills = userSkills.split(",").map{it.trim()}
        val arrayInterests = userInterests.split(",").map{it.trim()}
        val education = Education(degree, school)

        val skillsList = mutableListOf<Skills>()
        for (skillName in arraySkills) {
            val skill = Skills(skillName)
            skillsList.add(skill)
        }

        val interestsList = mutableListOf<Interests>()
        for (interestName in arrayInterests) {
            val interest = Interests(interestName)
            interestsList.add(interest)
        }

        val connectionList = mutableListOf<Connections>()

        val userProfile = UserProfile(displayName, phoneNumber, birthDate, male, location, education, bio,
            skillsList, interestsList, connectionList)
        val users = Users((userProfile))


        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val userId = it.uid
            val userRef = firestore.collection("users").document(userId)
            when {
                displayName.isEmpty() -> {
                    binding.inputName.error = "Masukkan Nama"
                    Toast.makeText(this, "Masukkan Nama", Toast.LENGTH_SHORT).show()

                }
                phoneNumber.isEmpty() -> {
                    binding.inputNumber.error = "Masukkan Nomor Telepon"
                    Toast.makeText(this, "Masukkan Nomor Telepon", Toast.LENGTH_SHORT).show()
                }
                birthDate.isEmpty() -> {
                    binding.inputNumber.error = "Masukkan Tanggal Lahir"
                    Toast.makeText(this, "Masukkan Tanggal Lahir", Toast.LENGTH_SHORT).show()
                }
                userSkills.isEmpty() -> {
                    binding.mactvSkills.error = "Masukkan Keahlian"
                    Toast.makeText(this, "Masukkan Keahlian", Toast.LENGTH_SHORT).show()
                }
                userInterests.isEmpty() -> {
                    binding.mactvInterests.error= "Masukkan Posisi Rekan yang di Inginkan"
                    Toast.makeText(this, "Masukkan Posisi Rekan yang di Inginkan", Toast.LENGTH_SHORT).show()
                }
                location.isEmpty() -> {
                    binding.inputAddress.error = "Masukkan Alamat"
                    Toast.makeText(this, "Masukkan Alamat", Toast.LENGTH_SHORT).show()
                }
                degree.isEmpty() -> {
                    binding.inputDegree.error = "Masukkan Gelar dan Jurusan"
                    Toast.makeText(this, "Masukkan Gelar dan Jurusan", Toast.LENGTH_SHORT).show()
                }
                school.isEmpty() -> {
                    binding.inputAddress.error = "Masukkan Sekolah atau Universitas"
                    Toast.makeText(this, "Masukkan Sekolah atau Universitas", Toast.LENGTH_SHORT).show()
                }
                bio.isEmpty() -> {
                    binding.inputAddress.error = "Masukkan Bio"
                    Toast.makeText(this, "Masukkan Bio", Toast.LENGTH_SHORT).show()
                }
                else -> {
                userRef.set(users, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT)
                            .show()
                        goToHomeActivity()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Profil gagal diperbarui", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
    private fun fetchSkills() {
        val skillsCollection = firestore.collection("skills")

        skillsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val skillsList = mutableListOf<String>()

                for (document in querySnapshot.documents) {
                    val skill = document.getString("name")
                    skill?.let { skillsList.add(it) }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, skillsList)
                binding.mactvSkills.setAdapter(adapter)
                binding.mactvSkills.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
            }
            .addOnFailureListener {
            }
    }

    private fun fetchInterests() {
        val interestsCollection = firestore.collection("interests")

        interestsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val interestsList = mutableListOf<String>()

                for (document in querySnapshot.documents) {
                    val interest = document.getString("name")
                    interest?.let { interestsList.add(it) }
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, interestsList)
                binding.mactvInterests.setAdapter(adapter)
                binding.mactvInterests.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
            }
            .addOnFailureListener {
            }
    }
    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}