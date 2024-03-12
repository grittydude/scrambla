
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.squash.R
import com.example.squash.adapters.SubCategoryAdapter
import com.example.squash.data.DataSource
import com.example.squash.databinding.FragmentSubCategory2Binding
import com.example.squash.datasource.SubCategories


class SubCategoryFragment : Fragment() {

    private lateinit var binding: FragmentSubCategory2Binding
    private  var subCategories = ArrayList<SubCategories>()
    private  var adapter =  SubCategoryAdapter(subCategories)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubCategory2Binding.inflate(inflater, container, false)

        val intent = this.arguments
        val getData = intent?.get("name")
        binding.text.text = getData.toString()

        when(getData){
            "Medicine" -> {
                val medicine =  DataSource().loadMedicineOccupation()
                subCategories = medicine
                adapter = SubCategoryAdapter(medicine)

            }
            "Sport" -> {
                val sport =  DataSource().loadSportOccupation()
                subCategories = sport
                adapter = SubCategoryAdapter(sport)
            }
            "Countries" -> {
                val countries =  DataSource().loadCountriesOccupation()
                subCategories = countries
                adapter = SubCategoryAdapter(countries)
            }

            "Food" -> {
                val food =  DataSource().loadFoodOccupation()
                subCategories = food
                adapter = SubCategoryAdapter(food)
            }
        }


        binding.backBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_subCategoryFragment_to_homeFragment)
        }
        val recycler = binding.subRecycler
        recycler.layoutManager = LinearLayoutManager(requireContext())
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
        return binding.root
    }

    private fun myFilter(newText: String?) {
        val filteredList : ArrayList<SubCategories> = ArrayList()
        for (item in subCategories){
            if (item.name.toLowerCase().contains(newText!!.toLowerCase())){
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(requireContext(), "Nothing found", Toast.LENGTH_SHORT).show()
        }else{
            adapter.listFilter(filteredList)
        }
    }
}