package com.umc.playkuround.fragment

import androidx.fragment.app.Fragment
//import com.umc.playkuround.databinding.FragmentRankingBinding

class RankingFragment : Fragment() {

//    lateinit var binding: FragmentRankingBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentRankingBinding.inflate(inflater, container, false)
//
//        setDataFromServer()
//
//        binding.rankingInfoIb.setOnClickListener {
//            val intent = Intent(context, RankingInfoActivity::class.java)
//            startActivity(intent)
//        }
//
//        return binding.root
//    }
//
//    private fun submitList() {
//        binding.rankingEmptyTv.isVisible = false
//        binding.rankingRecyclerView.isVisible = true
//        val items = binding.rankingRecyclerView.adapter?.itemCount
//
//        if (items == 0) {
//            binding.rankingEmptyTv.isVisible = true
//            binding.rankingRecyclerView.isVisible = false
//        }
//    }
//
//    private fun setDataFromServer() {
//        val loading = LoadingDialog(requireActivity())
//        loading.show()
//
//        val userService2 = UserService()
//        userService2.setOnResponseListener(object : UserService.OnResponseListener() {
//            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                if (isSuccess) {
//                    binding.rankingRecyclerView.adapter = RankingRVAdapter(body as ArrayList<Ranking>)
//
//                    val userService = UserService()
//                    userService.setOnResponseListener(object : UserService.OnResponseListener() {
//                        override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
//                            if (isSuccess) {
//                                val myRank = body as Ranking
//                                binding.rankingMyRankTv.text = myRank.ranking.toString()
//                                binding.rankingMyScoreTv.text = myRank.points.toString()
//                                loading.dismiss()
//                            } else {
//                                loading.dismiss()
//                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }).getUserRanking(user.getAccessToken())
//                } else {
//                    binding.rankingRecyclerView.adapter = RankingRVAdapter(ArrayList())
//                    loading.dismiss()
//                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
//                }
//                submitList()
//            }
//        }).getTop100Ranking(user.getAccessToken())
//    }

}