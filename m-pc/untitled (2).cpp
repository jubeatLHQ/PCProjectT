#include <iostream>

using namespace std;
int height, width, tmp, sx, sy, mx, my, tx, ty;//sx,syは動かすマスの座標 mx,myは上下左右の動作量
bool check();                   //ゴール状態か判定
static int state[16][16];             //盤面の状態を保存
static int goal[16][16];                  //ゴールの盤面を保存
int move(int x, int y, int flag);


//交換操作
int swap_U(int state[16][16], int sy, int sx){
    tmp = state[sy-1][sx];
    state[sy-1][sx] = state[sy][sx];
    state[sy][sx] = tmp;
    cout << "U";
    return 0;
}
int swap_D(int state[16][16], int sy, int sx){
    tmp = state[sy+1][sx];
    state[sy+1][sx] = state[sy][sx];
    state[sy][sx] = tmp;
    cout << "D";
    return 0;
}
int swap_R(int state[16][16], int sy, int sx){
    tmp = state[sy][sx+1];
    state[sy][sx+1] = state[sy][sx];
    state[sy][sx] = tmp;
    cout << "R";
    return 0;
}
int swap_L(int state[16][16], int sy, int sx){
    tmp = state[sy][sx-1];
    state[sy][sx-1] = state[sy][sx];
    state[sy][sx] = tmp;
    cout << "L";
    return 0;
}
/*
 ---------簡易表---------
 (0,0) (0,1) (0,1) (0,1)
 (1,0) (1,1) (1,2) (1,3)
 (2,0) (2,1) (2,2) (2,3)
 (3,0) (3,1) (3,2) (3,3)
1 2 3 25 5
6 8 7 9 10
11 12 13 14 15
16 17 18 19 20
21 22 23 4 24

1 2 3
4 5 6
8 10 15
9 12 11
7 14 13
*/
int main(){
    int space, k = 1;
    cout << "height:";
    cin >> height;
    cout << "width:";
    cin >> width;
    cout << "space:";
    cin >> space;
    for(short i = 0; i < height; i++){
        for(short j = 0; j < width; j++){
            cin >> state[i][j];
            goal[i][j] = k;
            k++;
            if(state[i][j] == space){
                sx = j;
                sy = i;
            }
        }
    }
    /*
    for(short i = 0; i < 4;i++){
        for(short j = 0; j < 4;j++){
            if(j != 3)  move(i,j,0);
            if(j == 3)  move(i,j,1);
        }
    }
    */
    for(int i = 0; i < height-2; i++){
        for(int j = 0; j < width; j++){
            if(j == width-1){
                move(i,j,1);
            }else{
                move(i,j,0);
            }
        }
    }
    cout << endl;
    for(int i = 0; i < height; i++){
        for(int j = 0; j < width; j++){
            cout << state[i][j] << " ";
        }
        cout << endl;
    }

    return 0;
}


int move(int y, int x, int flag){
    //動かす必要が無い場合返す
    if(goal[y][x] == state[y][x]){
        return 0;
    }
    //必要な上下左右の動作を確認
    for(int i = 0; i < height; i++){
        for(int j = 0; j < width; j++){
            if(goal[y][x] == state[i][j]){
                tx = j;
                ty = i;
                mx = x-j;
                my = y-i;
            }
        }
    }
    
    //右端を埋めて行くコード
    if(flag == 1){
        //もしも左端に右端に埋めるための数字があるなら
        if(tx == 0){
            while(sy != ty){
                swap_D(state, sy, sx);
                sy++;
            }
            while(sx != 0){
                swap_L(state, sy, sx);
                sx--;
            }
            mx--;
            tx++;
        }else if(tx != 0){
            swap_D(state, sy, sx);
            sy++;
            if(state[sy-1][sx] == goal[y][x]){
                ty--;
            }
            if(tx != sx && ty == sy){
               while(sy != ty){
                    swap_D(state, sy, sx);
                    sy++;
                }
                while(sx != 0){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }else if(tx != sx && ty != sy){
                while(sx != 0){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }else if(tx == sx && (ty-1) >= sy){
                while(sx != 0){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }else if(tx == sx && (ty+1) == sy){
                my++;
                ty++;
                while(sx != 0){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }
        }
        while(sy != y){
        swap_U(state, sy, sx);
            sy--;
        }
        while(sx != width-1){
            swap_R(state, sy, sx);
            sx++;
        }
    }

    //必要な上下左右の動作を”再”確認
    for(int i = 0; i < height; i++){
        for(int j = 0; j < width; j++){
            if(goal[y][x] == state[i][j]){
                tx = j;
                ty = i;
                mx = x-j;
                my = y-i;
            }
        }
    }




    /*

     2  3  5  4
     1 16  7  8
     6  9 10 14
    11 13 12 15
    cout << "mx:" << mx << "my:" << my << endl;
    cout << "sx:" << sx << "sy:" << sy << endl;
    cout << "tx:" << tx << "ty:" << ty << endl;
    */
if(state[y][x] != goal[y][x]){
    //選択パネルが,動かすパネルより上
    if(sy < ty){
        //選択パネルを,動かすパネルのひとつ上の列まで持って行く
        while(sy != (ty-1)){
            swap_D(state, sy, sx);
            sy++;
        }
        //選択パネルが,動かすパネルより右にあり
        if(sx > tx){
            //目的地より右にある場合
            if(mx <= -1){
                //動かすパネルの左上まで持って行く
                while(tx != sx-1){
                    swap_L(state, sy, sx);
                    sx--;
                }
                //動かすパネルの左横に持って行く
                swap_D(state, sy, sx);
                sy++;
            //目的地より左にある場合
            }else if(mx >= 1){
                swap_D(state, sy, sx);
                sy++;
                //右横に持って行く
                while((tx+1) != sx){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }else if(mx == 0){
                while(tx != sx){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }
        /*-----------ここまで完成！！！----------*/
        //選択パネルが,動かすパネルより左
        }else if(sx < tx){
            //選択パネルが,動かすパネルより左にあり
            if(sx < tx){
                //目的地より左にある場合
                if(mx >= 1){
                    //選択パネルを動かすパネルの真上へ
                    while((tx+1) != sx){
                        swap_R(state, sy, sx);
                        sx++;
                    }
                    swap_D(state, sy, sx);
                    sy++;
                //目的地より右にある場合
                }else if(mx <= -1){
                    swap_D(state, sy, sx);
                    sy++;
                    while(tx != sx+1){
                        swap_R(state, sy, sx);
                        sx++;
                    }
                }else if(mx == 0){
                    while(tx != sx){
                        swap_R(state, sy, sx);
                        sx++;
                    }
                }
            }

        }else if(sx == tx){
            if(mx <= -1){
                swap_L(state, sy, sx);
                sx--;
                swap_D(state, sy, sx);
                sy++;
            }else if(mx >= 1){
                swap_R(state, sy, sx);
                sx++;
                swap_D(state, sy, sx);
                sy++;
            }   
        }
    /*-----------ここまで完成！----------*/
    //選択パネルが,動かすパネルより下
    }else if(sy > ty){
        while(sy != (ty+1)){
            swap_U(state, sy, sx);
            sy--;
        }
        //選択パネルが,動かすパネルより右にあり
        if(sx > tx){
            //目的地より左にある場合
            if(mx >= 1){
                //動かすパネルの右下へ
                while(sx != (tx+1)){
                    swap_L(state, sy, sx);
                    sx--;
                }
            //動かすパネルの右横へ
            swap_U(state, sy, sx);
            sy--;
            //目的地より右にある場合
            }else if(mx <= -1){
                //左下へ
                while(sx != (tx-1)){
                    swap_L(state, sy, sx);
                    sx--;
                }
                //左横へ
                swap_U(state, sy, sx);
                sy--;
            }else if(mx == 0){
                while(sy != ty-1){
                    swap_U(state, sy, sx);
                    sy--;
                }
                while(sx != tx){
                    swap_L(state, sy, sx);
                    sx--;
                }
            }
        //選択パネルが,動かすパネルより左にあり
        }else if(sx < tx){
            //目的地より左にある場合
            if(mx >= 1){
                //動かすパネルの右下へ
                while(sx != (tx+1)){
                    swap_R(state, sy, sx);
                    sx++;
                }           
                //パネルの右横へ
                swap_U(state, sy, sx);
                sy--;
            //目的地より右にある場合
            }else if(mx <= 0){
                //パネルの左下へ
                while(sx != tx-1){
                    swap_R(state, sy, sx);
                    sx++;
                }
                //パネルの左横へ
                swap_U(state, sy, sx);
                sy--;
            }
        }else if(sx == tx){
            //パネルの左横へ
            if(mx <= -1){
                swap_L(state, sy, sx);
                sx--;
                swap_U(state, sy, sx);
                sy--;
            //右横へ
            }else if(mx >= 1){
                swap_R(state, sy, sx);
                sx++;
                swap_U(state, sy, sx);
                sy--;
            //反時計回りに真上へ
            }else if(mx == 0 && sx != width-1){
                swap_R(state, sy, sx);
                sx++;
                swap_U(state, sy, sx);
                sy--;
                swap_U(state, sy, sx);
                sy--;
                swap_L(state, sy, sx);
                sx--;
            //時計回りに真上へ
            }else if(mx == 0 && sx == width-1){
                swap_L(state, sy, sx);
                sx--;
                swap_U(state, sy, sx);
                sy--;
                swap_U(state, sy, sx);
                sy--;
                swap_R(state, sy, sx);
                sx++;
            }
        }
    //選択パネルが動かすパネルと同じ列
    }else if(sy == ty){
        //選択パネルが動かすパネルより右
        if(sx > tx){
            while(sx != tx+1){
                swap_L(state, sy, sx);
                sx--;
            }
            if(mx <= -1){
                if(sy == height-1){
                    swap_U(state, sy, sx);
                    sy--;
                    swap_L(state, sy, sx);
                    sx--;
                    swap_L(state, sy, sx);
                    sx--;
                    swap_D(state, sy, sx);
                    sy++;
                }else if(sy != height-1){
                    swap_D(state, sy, sx);
                    sy++;
                    swap_L(state, sy, sx);
                    sx--;
                    swap_L(state, sy, sx);
                    sx--;
                    swap_U(state, sy, sx);
                    sy--;
                }
            }else if(mx == 0){
                swap_U(state, sy, sx);
                sy--;
                swap_L(state, sy, sx);
                sx--;

            }
        //選択パネルが動かすパネルより左
        }else if(sx < tx){
            while(sx != tx-1){
                swap_R(state, sy, sx);
                sx++;
            }
            if(mx >= 1){
                if(sy != height-1){
                    swap_D(state, sy, sx);
                    sy++;
                    swap_R(state, sy, sx);
                    sx++;
                    swap_R(state, sy, sx);
                    sx++;
                    swap_U(state, sy, sx);
                    sy--;
                }else if(sy == height-1){
                    swap_U(state, sy, sx);
                    sy--;
                    swap_R(state, sy, sx);
                    sx++;
                    swap_R(state, sy, sx);
                    sx++;
                    swap_D(state, sy, sx);
                    sy++;
                }
            }else if(mx == 0 && sy == height-1){
                swap_U(state, sy, sx);
                sy--;
                swap_R(state, sy, sx);
                sx++;
            }else if(mx == 0 && sy != height-1){
                swap_D(state, sy, sx);
                sy++;
                swap_R(state, sy, sx);
                sx++;
                swap_R(state, sy, sx);
                sx++;
                swap_U(state, sy, sx);
                sy--;
                swap_U(state, sy, sx);
                sy--;
                swap_L(state, sy, sx);
                sx--;
            }
        }
    }
    //以上、ここまでが交換する直前まで持って行くプログラム

    /*
    cout << '\n';
    cout << "mx:" << mx << "my:" << my << endl;
    cout << "sx:" << sx << "sy:" << sy << endl;
    cout << "tx:" << tx << "ty:" << ty << endl;
    */

    //ここからが交換するプログラム

    //スペースが左にある場合
    if(tx == sx+1 && mx != 0){
        while(mx != 0){
            swap_R(state, sy, sx);
            sx++;
            mx++;
            tx--;
            if(sy != height-1 && mx != 0){
                swap_D(state, sy, sx);
                sy++;
                swap_L(state, sy, sx);
                sx--;
                swap_L(state, sy, sx);
                sx--;
                swap_U(state, sy, sx);
                sy--;
            }else if(sy == height-1 && mx != 0){
                swap_U(state, sy, sx);
                sy--;
                swap_L(state, sy, sx);
                sx--;
                swap_L(state, sy, sx);
                sx--;
                swap_D(state, sy, sx);
                sy++;
            }
        }
    }
    //スペースが右にある場合
    if(tx == sx-1 && mx != 0){
        while(mx != 0){
            swap_L(state, sy, sx);
            sx--;
            mx--;
            tx++;
            if(sy != height-1 && mx != 0){
                swap_D(state, sy, sx);
                sy++;
                swap_R(state, sy, sx);
                sx++;
                swap_R(state, sy, sx);
                sx++;
                swap_U(state, sy, sx);
                sy--;
            }else if(sy == height-1 && mx != 0){
                swap_U(state, sy, sx);
                sy--;
                swap_R(state, sy, sx);
                sx++;
                swap_R(state, sy, sx);
                sx++;
                swap_D(state, sy, sx);
                sy++;
            }
        }
    }
    //mxが0だがmyが0じゃない&&右にある
    if(mx == 0 && my != 0 && tx+1 == sx){
        swap_U(state, sy, sx);
        sy--;
        swap_L(state, sy, sx);
        sx--;
    //mxが0だがmyが0じゃない&&左にある
    }else if(mx == 0 && my != 0 && tx-1 == sx){
        if(sy == height-1){
            swap_U(state, sy, sx);
            sy--;
            swap_R(state, sy, sx);
            sx++;
        }else if(sy != height-1 && tx != width-1){
            swap_D(state, sy, sx);
            sy++;
            swap_R(state, sy, sx);
            sx++;
            swap_R(state, sy, sx);
            sx++;
            swap_U(state, sy, sx);
            sy--;
            swap_U(state, sy, sx);
            sy--;
            swap_L(state, sy, sx);
            sx--;
        }else if(sy != height-1 && tx == width-1){
            swap_U(state, sy, sx);
            sy--;
            swap_R(state, sy, sx);
            sx++;
        }
    }
    //選択したパネルが真上にあるとき
    if(ty == sy+1 && my <= -1 && mx == 0){
        while(my != 0){
            swap_D(state, sy, sx);
            sy++;
            my++;
            if(sx != width-1 && my <= -1){
                swap_R(state, sy, sx);
                sx++;
                swap_U(state, sy, sx);
                sy--;
                swap_U(state, sy, sx);
                sy--;
                swap_L(state, sy, sx);
                sx--;
            }else if(sx == width-1 && my <= -1){
                swap_L(state, sy, sx);
                sx--;
                swap_U(state, sy, sx);
                sy--;
                swap_U(state, sy, sx);
                sy--;
                swap_R(state, sy, sx);
                sx++;
            }
        }
    }

    if(flag == 1){
        if(sx == width-1){
            swap_L(state, sy, sx);
            sx--;
        }
        /*おそらくいらない
        }else if(sx <= 2){
            while(sx != (3-1)){
                swap_R(state, sy, sx);
                sx++;
            }
        }
        */
        while(sy != y){
            swap_U(state, sy, sx);
            sy--;
        }
        while(sx != 0){
            swap_L(state, sy, sx);
            sx--;
        }

        swap_D(state, sy, sx);
        sy++;
    }

}




    return 0;
}

bool check(){
    for(int i = 0; i < height; i++){
        for(int j = 0; j < width; j++){
            if(state[i][j] != goal[i][j]){
                return false;
            }
        }
    }
    return true;
}

/*

13 14 10  3 
12 11  4  9 
 8  7  6  5 
15  1  2 16         
            swap_R(state, sy, sx);
            sx++;
            swap_D(state, sy, sx);
            sy++;
            swap_L(state, sy, sx);
            sx--;
            swap_U(state, sy, sx);
            sy--;

*/