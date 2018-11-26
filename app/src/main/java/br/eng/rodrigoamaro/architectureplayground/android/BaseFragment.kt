package br.eng.rodrigoamaro.architectureplayground.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import br.eng.rodrigoamaro.architectureplayground.redux.Interactor
import br.eng.rodrigoamaro.architectureplayground.redux.State
import br.eng.rodrigoamaro.architectureplayground.redux.Store

abstract class BaseFragment<S : State, V : ViewModel> : Fragment() {

    private lateinit var interactor: Interactor

    abstract val modelClass: Class<V>

    protected lateinit var model: V

    abstract val layoutResId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity as BaseActivity<S>
        model = ViewModelProviders.of(this).get(modelClass)
        interactor = createInteractor(act.store, view)
        interactor.turnOn()
    }

    abstract fun createInteractor(
        store: Store<S>,
        view: View
    ): Interactor

    override fun onDestroyView() {
        interactor.turnOff()
        super.onDestroyView()
    }
}