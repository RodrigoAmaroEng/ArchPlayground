package br.eng.rodrigoamaro.architectureplayground.coffee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import br.eng.rodrigoamaro.architectureplayground.R
import br.eng.rodrigoamaro.architectureplayground.SimpleInteractor

class PaymentFragment : Fragment() {
    private lateinit var interactor: SimpleInteractor<SaleState>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity as MainActivity
        interactor = PaymentInteractor(act.store, view, NavHostFragment.findNavController(this))
        interactor.turnOn()
    }

    override fun onDestroyView() {
        interactor.turnOff()
        super.onDestroyView()
    }

}