package com.grittydude.brainscrambla.activities
import com.grittydude.brainscrambla.data.DataSource
import com.grittydude.brainscrambla.adapters.SubCategoryAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.grittydude.brainscrambla.databinding.ActivitySubCategoryBinding
import com.grittydude.brainscrambla.datasource.SubCategories

class SubCategory : AppCompatActivity() {

    private lateinit var subCategories: ArrayList<SubCategories>
    private lateinit var adapter: SubCategoryAdapter

    private lateinit var binding: ActivitySubCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySubCategoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent.extras
        val getData = intent?.get("name")
        binding.text.text = getData.toString()


        val sport =  DataSource().loadSportOccupation()
        val countries =  DataSource().loadCountriesOccupation()



        when(getData){
            "Medicine" -> {

                val medicine =  DataSource().loadMedicineOccupation()
                subCategories = medicine
                adapter = SubCategoryAdapter(medicine)

            }
            "Sport" -> adapter = SubCategoryAdapter(sport)
            "Countries" -> adapter = SubCategoryAdapter(countries)
        }


//        subCategories.add(SubCategories("Nursing"))
//        subCategories.add(SubCategories("Nursing"))
//        subCategories.add(SubCategories("Medicine"))
//        subCategories.add(SubCategories("Shoe"))
//        subCategories.add(SubCategories("Nursing"))
//        subCategories.add(SubCategories("Nursing"))



        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        val recycler = binding.subRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               myFilter(newText)
                return false
            }
        })

    }

    private fun myFilter(newText: String?) {
        val filteredList : ArrayList<SubCategories> = ArrayList()
        for (item in subCategories){
            if (item.name.toLowerCase().contains(newText!!.toLowerCase())){
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(applicationContext, "Nothing found", Toast.LENGTH_SHORT).show()
        }else{
            adapter.listFilter(filteredList)
        }
    }


}