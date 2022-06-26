package com.mango.convexhull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Point> points=new ArrayList<Point>();
    ArrayList<Point> outp = new ArrayList<Point>();
    ConstraintLayout LL;
    Button save,run,reset,p5,p7;
    EditText inputPointx,inputPointy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LL=findViewById(R.id.layoutt);
       save=findViewById(R.id.btnSave);
       run=findViewById(R.id.btnAlgo);
       reset=findViewById(R.id.btnReset);
       p5=findViewById(R.id.rand5);
       p7=findViewById(R.id.rand7);
       inputPointx=findViewById(R.id.txtPoints);
       inputPointy=findViewById(R.id.txtPoints2);

        save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int x=Integer.parseInt(inputPointx.getText().toString())*10;
               int y=(Integer.parseInt(inputPointy.getText().toString())*10)+500;
               PointView tempPoint =new PointView(MainActivity.this);
               tempPoint.setPointA(new PointF(x,y));
               tempPoint.setColor(1);
               points.add(new Point(x,y));
                LL.addView(tempPoint);

                inputPointx.setText("");
                inputPointy.setText("");
           }
       });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outp=quickHull(points);
                Point next = new Point();
                if (outp.size()<3){
                    Toast.makeText(MainActivity.this, "Points are too lower ADD more than 2 Points", Toast.LENGTH_LONG).show();
                return;
                }
                for (int i=0;i<outp.size();i++){
                    LineView tempLine= new LineView(MainActivity.this);
                    if (i==outp.size()-1) {
                        next.x=outp.get(0).x;
                        next.y=outp.get(0).y;
                    }else {
                        next.x=outp.get(i+1).x;
                        next.y=outp.get(i+1).y;
                    }

                    tempLine.setPointA(new PointF(outp.get(i).x,outp.get(i).y));
                    tempLine.setPointB(new PointF(next.x,next.y));
                    LL.addView(tempLine);


                }

                for (int i=0;i<outp.size();i++){

                    PointView tempPoint =new PointView(MainActivity.this);
                    tempPoint.setPointA(new PointF(outp.get(i).x,outp.get(i).y));
                    tempPoint.setColor(0);
                    LL.addView(tempPoint);
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        p5.setOnClickListener(v->{
            GerateRandomPoint(5);
        });
        p7.setOnClickListener(v->{
            GerateRandomPoint(7);
        });
    }

    ///
    public ArrayList<Point> quickHull(ArrayList<Point> points)
    {
        ArrayList<Point> convexHull = new ArrayList<Point>();
        if (points.size() < 3)
            return (ArrayList) points.clone();

        int minPoint = -1, maxPoint = -1;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i).x < minX)
            {
                minX = points.get(i).x;
                minPoint = i;
            }
            if (points.get(i).x > maxX)
            {
                maxX = points.get(i).x;
                maxPoint = i;
            }
        }
        Point A = points.get(minPoint);
        Point B = points.get(maxPoint);
        convexHull.add(A);
        convexHull.add(B);
        points.remove(A);
        points.remove(B);

        ArrayList<Point> leftSet = new ArrayList<Point>();
        ArrayList<Point> rightSet = new ArrayList<Point>();

        for (int i = 0; i < points.size(); i++)
        {
            Point p = points.get(i);
            if (pointLocation(A, B, p) == -1)
                leftSet.add(p);
            else if (pointLocation(A, B, p) == 1)
                rightSet.add(p);
        }
        hullSet(A, B, rightSet, convexHull);
        hullSet(B, A, leftSet, convexHull);

        return convexHull;
    }

    ///
    public int distance(Point A, Point B, Point C)
    {
        int ABx = B.x - A.x;
        int ABy = B.y - A.y;
        int num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
        if (num < 0)
            num = -num;
        return num;
    }

    ///
    public void hullSet(Point A, Point B, ArrayList<Point> set,
                        ArrayList<Point> hull)
    {
        int insertPosition = hull.indexOf(B);
        if (set.size() == 0)
            return;
        if (set.size() == 1)
        {
            Point p = set.get(0);
            set.remove(p);
            hull.add(insertPosition, p);
            return;
        }
        int dist = Integer.MIN_VALUE;
        int furthestPoint = -1;
        for (int i = 0; i < set.size(); i++)
        {
            Point p = set.get(i);
            int distance = distance(A, B, p);
            if (distance > dist)
            {
                dist = distance;
                furthestPoint = i;
            }
        }
        Point P = set.get(furthestPoint);
        set.remove(furthestPoint);
        hull.add(insertPosition, P);

        // Determine who's to the left of AP
        ArrayList<Point> leftSetAP = new ArrayList<Point>();
        for (int i = 0; i < set.size(); i++)
        {
            Point M = set.get(i);
            if (pointLocation(A, P, M) == 1)
            {
                leftSetAP.add(M);
            }
        }

        // Determine who's to the left of PB
        ArrayList<Point> leftSetPB = new ArrayList<Point>();
        for (int i = 0; i < set.size(); i++)
        {
            Point M = set.get(i);
            if (pointLocation(P, B, M) == 1)
            {
                leftSetPB.add(M);
            }
        }
        hullSet(A, P, leftSetAP, hull);
        hullSet(P, B, leftSetPB, hull);

    }

    ///
    public int pointLocation(Point A, Point B, Point P)
    {
        int cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
        if (cp1 > 0)
            return 1;
        else if (cp1 == 0)
            return 0;
        else
            return -1;
    }

    ///
    public void GerateRandomPoint(int z){
        Random random= new Random();
        for (int i=0;i<=z;i++){
            int x=random.nextInt(700)+22;
            int y=random.nextInt(700)+400;
            PointView tempPoint =new PointView(MainActivity.this);
            tempPoint.setPointA(new PointF(x,y));
            tempPoint.setColor(1);
            points.add(new Point(x,y));
            LL.addView(tempPoint);

        }
    }

}