package com.example.exercise.ui.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import com.example.exercise.R
import com.example.exercise.data.network.RandomJokeRepository
import com.example.exercise.data.util.JokeResponse
import com.example.exercise.databinding.JokesDisplayMainFragmentBinding
import com.example.exercise.ui.MainApplication
import com.example.exercise.ui.util.CommonUtil
import com.example.exercise.ui.viewModel.JokeViewModel
import com.example.exercise.ui.viewModel.JokeViewModelFactory
import javax.inject.Inject

class JokesDisplayMainFragment : Fragment(), JokesViewPager.GestureEvents {
    private lateinit var layoutBinding: JokesDisplayMainFragmentBinding
    private lateinit var jokesViewPagerAdapter: JokesViewPagerAdapter
    private val jokesList: ArrayList<String> = ArrayList()
    private var jokeViewModel: JokeViewModel? = null
    private var currentPage: Int = 0
    private lateinit var firstName: String
    private lateinit var lastName: String
    @Inject lateinit var randomJokeRepository: RandomJokeRepository // used Dagger2 dependency injection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = JokesDisplayMainFragmentBinding.inflate(inflater, container, false)
        return layoutBinding.root

    }

    fun setFirstNameAndLastName(firstName: String, lastName: String) {
        this.firstName = firstName
        this.lastName = lastName

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity?.application as MainApplication).randomJokeRepositoryComponent.inject(this)
//        val randomJokeWebService = RetrofitRandomJoke().getApi(RandomJokeWebService::class.java)
//        val randomJokeRepository = RandomJokeRepository(randomJokeWebService)
        val factory = JokeViewModelFactory(randomJokeRepository, firstName, lastName)
        jokeViewModel = ViewModelProvider(this, factory).get(JokeViewModel::class.java)
        enableProgressBar()
        setObserverOnJokeViewModel(jokeViewModel?.jokeResponse)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutBinding.jokesViewPager.gestureEventListener = this
        jokesViewPagerAdapter = JokesViewPagerAdapter(childFragmentManager, jokesList)
        layoutBinding.jokesViewPager.adapter = jokesViewPagerAdapter
        jokesViewPagerAdapter.addJoke(" ") // adds empty page which will be replaced with setJoke in ViewModel observer callback
    }

    fun setObserverOnJokeViewModel(jokeResponse: LiveData<JokeResponse>?) {
        jokeResponse?.observe(viewLifecycleOwner, Observer { response ->
            if (response.result == CommonUtil.RESULT_SUCCESS) {
                jokesViewPagerAdapter.setJoke(currentPage, response.joke!!)
                /* Empty page is added for avoiding user seeing page reach end scroll bar when user performs right to left gesture
                 in the last page of the ViewPager */
                jokesViewPagerAdapter.addJoke(" ")
                layoutBinding.jokesViewPager.setCurrentItem(jokesViewPagerAdapter.count - 2, true)

            } else { // error case handling 
                layoutBinding.jokesViewPager.setCurrentItem(--currentPage)
                val context = this@JokesDisplayMainFragment.activity as Context
                if (response.isNetworkError) {
                    CommonUtil.showInternetConnectionError(context)
                } else {
                    val title: String = context.getString(R.string.error)
                    CommonUtil.showDialog(
                        context,
                        title,
                        response.errorMessage
                    )
                }
            }
            layoutBinding?.progressBar?.visibility = View.GONE
        })
    }

    companion object {
        const val ARG_JOKE = "object"
    }

    class JokesViewPagerAdapter(fm: FragmentManager, jokesList: ArrayList<String>) :
        FragmentStatePagerAdapter(fm) {

        private var jokesList = jokesList

        override fun getCount(): Int {
            return jokesList.size
        }

        fun addJoke(joke: String) {
            jokesList.add(joke)
            notifyDataSetChanged()
        }

        fun setJoke(position: Int, joke: String) {
            if (jokesList.size >= position) {
                jokesList.set(position, joke)
            }
        }

        override fun getItem(i: Int): Fragment {
            val fragment = RandomJokeDisplayFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_JOKE, jokesList.get(i))
            }
            return fragment
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

    }

    // This method is called from JokesViewPager class when right to left gesture happens
    override fun onSwipeRight() {
        // Load new joke when there is still one page left to show in ViewPager, this avoids showing of end reached scroll bar 
        if (layoutBinding.jokesViewPager.currentItem == jokesViewPagerAdapter.count - 2) {
            currentPage =
                layoutBinding.jokesViewPager.currentItem // gets the current page index from adapter
            currentPage = currentPage!! + 1
            getJokeFromJokeViewModel()
        } else if (jokesViewPagerAdapter.count == 1) { /* This case comes when HttpError or No Internet connection Error 
        comes for the first page*/
            currentPage = 0
            getJokeFromJokeViewModel()
        }
    }

    fun getJokeFromJokeViewModel() {
        enableProgressBar()
        jokeViewModel?.getRandomJoke()
    }

    fun enableProgressBar() {
        layoutBinding?.progressBar?.visibility = View.VISIBLE
    }

    // This should be called from parent activity in onBackPressed() method .
    fun onActivityBackPressed(): Boolean {
        if (layoutBinding.jokesViewPager.currentItem == 0)
            return true
        else {
            layoutBinding.jokesViewPager.setCurrentItem(layoutBinding.jokesViewPager.currentItem - 1, true)
            return false
        }
    }
}
    