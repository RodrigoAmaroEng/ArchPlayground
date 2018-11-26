package br.eng.rodrigoamaro.architectureplayground.coffee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.SimpleInteractor

class CoffeeFragment : Fragment() {
    private lateinit var interactor: SimpleInteractor<SaleState>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coffees, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity as MainActivity
        val model = ViewModelProviders.of(this).get(ViewState::class.java)
        interactor = CoffeeInteractor(act.store, view, model)
        interactor.turnOn()
    }

    override fun onDestroyView() {
        interactor.turnOff()
        super.onDestroyView()
    }
}