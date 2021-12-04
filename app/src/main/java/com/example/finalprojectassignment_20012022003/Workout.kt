package com.example.finalprojectassignment_20012022003

class Workout(
    var name : String,
    var imageString: String,
    var music : Int,
    var isDone : Boolean,
    var minute : Int,
    var burnedCalorie : Int
        ){
    var id : String = ""
    init{
        id = idGenerator()
    }

    companion object{
        fun idGenerator():String{
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..10)
                .map { allowedChars.random() }
                .joinToString("")
        }
        fun getWorkout(id:String):Workout?{
            for(i in 0 until WorkoutActivity.arrayList.size){
                if(WorkoutActivity.arrayList[i].id == id){
                    return WorkoutActivity.arrayList[i]
                }
            }
            return null
        }
    }
}