package edu.csh.cj;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
/**
 * Created by Paul on 3/25/2015.
 */
public class UsersData extends Model{


    @Table(name = "Categories")
    public class Category extends Model {
        @Column(name = "Name")
        public String name;

        //Category restaurants = new Category();
        //restaurants.name() = "Restaurants"
       // restaurants.save()
    }

    //Category restaurants = new Category();
    //restaurants.name() = "Restaurants"
    //restaurants.save()

/*
    @Table(name = "Rankings")
    public class Ranks extends Model {
        @Column(name = "Score")
        public int score;
    }

    Ranks ScoreBoard = new Ranks();
    ScoreBoard.score = 5
    ScoreBoard.save()

*/
}
