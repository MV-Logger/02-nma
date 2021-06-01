package be.howest.maartenvercruysse.logger

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import be.howest.maartenvercruysse.logger.databinding.ActivityMainBinding
import be.howest.maartenvercruysse.logger.ui.MainViewModel
import be.howest.maartenvercruysse.logger.ui.MainViewModelFactory
import be.howest.maartenvercruysse.logger.ui.books.BookFragment
import be.howest.maartenvercruysse.logger.ui.books.BookFragmentDirections
import be.howest.maartenvercruysse.logger.ui.home.HomeFragment
import be.howest.maartenvercruysse.logger.ui.home.HomeFragmentDirections
import be.howest.maartenvercruysse.logger.work.RefreshDataWorker
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, MainViewModelFactory(this.application))
            .get(MainViewModel::class.java)


        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.bookFragment
            ), drawerLayout
        )

        initDrawerButton(drawerLayout, binding.appBarMain.toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val menu = navView.menu.addSubMenu("Books")

        viewModel.repo.books.observe(this, { list ->
            list?.let { m ->
                menu.clear()
                m.forEach {
                    menu.add(0, it.id, 0, it.name)
                }
            }
        })

        navView.setNavigationItemSelectedListener {
            val id = it.itemId
            val currTitle = it.title.toString()

            it.isCheckable = false // disable focus

            val fragment = getForegroundFragment()

            // This logic, controls which fragment will be opened when an item is clicked
            val action: NavDirections = if (id == R.id.nav_home) { // home button
                if (fragment !is HomeFragment) { // if not currently home
                    BookFragmentDirections.actionBookFragmentToHomeFragment() // go to home
                } else {
                    drawerLayout.closeDrawers()
                    return@setNavigationItemSelectedListener false
                }
            } else {
                when (fragment) { // BookFragments
                    is HomeFragment -> HomeFragmentDirections.actionHomeFragmentToBookFragment(id, currTitle)
                    is BookFragment -> BookFragmentDirections.actionBookFragmentSelf(id, currTitle)
                    else -> throw NullPointerException("Unknown fragment (shouldn't be possible)")
                }
            }
            navController.navigate(action)
            drawerLayout.closeDrawers()

            return@setNavigationItemSelectedListener true
        }
        navView.getHeaderView(0).findViewById<TextView>(R.id.title_nav).text = viewModel.repo.getUsername()
        delayedInit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                viewModel.repo.logout()
                this.finish()
            }
            R.id.action_webview -> openWebView()
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getForegroundFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    private fun initDrawerButton(drawerLayout: DrawerLayout, toolbar: Toolbar) {
        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {}
        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(drawerToggle)

    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun openWebView() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://logs-e7c66.web.app"))

        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            showToast(R.string.browser_not_found)
        }
    }

    private fun showToast(resource: Int) {
        Toast.makeText(this, resources.getString(resource), Toast.LENGTH_LONG).show()
    }
}