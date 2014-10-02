#include <iostream>
#include <string>
#include <map>
#include <queue>
#include <vector>
#include <algorithm>

#include <time.h>

using namespace std;


unsigned long countAll = 0;
int d[4] = {1, -1 ,3, -3};
string ds[4] = {"migi","hidari","shita","ue"};
int width = 3;
int height = 3;
int chooseLimit = 4;

struct P;
vector<vector<int> > setup();
int solver(P s);
int bfs(P s);


struct P{
    string panel;
    int space, cnt ,before,choose;
    string memo;
    P(string panel_, int space_){
        panel = panel_; space = space_;
	   choose = 0;
	   cnt = 0;
	   before = -1;
    }
	P(string panel_,int before_,int space_,int cnt_,int choose_,string memo_){
		panel = panel_;
		before = before_;
		space = space_;
		cnt = cnt_;
		choose = choose_;
		memo = memo_;
	}
	P(){}
};
bool judge(string s){
    for(int i = 0 ; i < width*height ; i++ ){
		
        if(s[i] != '1' + i )
            return false;
    }
	return true;
}
bool judgeMove(int now,int to){
	if(width==3&&height==3){
		if(now==0||now==1||now==2){
			if(to==-3){return false;}
		}
		if(now==2||now==5||now==8){
			if(to==1){return false;}
		}
		if(now==6||now==7||now==8){
			if(to==3){return false;}
		}
		if(now==0||now==3||now==6){
			if(to==-1){return false;}
		}
		return true;
	}else if(width==4&&height==4){
		if(now==0||now==1||now==2||now==3){
			if(to==-3){return false;}
		}
		if(now==3||now==7||now==11||now==15){
			if(to==1){return false;}
		}
		if(now==12||now==13||now==14||now==15){
			if(to==3){return false;}
		}
		if(now==0||now==4||now==8||now==12){
			if(to==-1){return false;}
		}
		return true;
	}
}


int getDistance(int nowAt,int to){
	int x,y;
	int tox,toy;
	int dist;
	y = nowAt/width;
	x = nowAt%width;
	toy = to/width;
	tox = to%width;
	dist = max(y,toy)-min(y,toy)+max(x,tox)-min(x,tox);
	return dist;
}

P getMovedP(P now,int nowat,int direction,string dirs,int nowDistance,bool isAround){
		
		  P nullP;
		  int idx2 = nowat + direction;
		  if(idx2==now.before){return nullP;}
		  if(!judgeMove(nowat,direction)) {return nullP;}
		  if(isAround==0&&getDistance(idx2,now.panel[now.space]-'1')>=nowDistance){return nullP;}


            string next_panel = now.panel;
            next_panel[idx2] = now.panel[nowat];
            next_panel[nowat] = now.panel[idx2];
            P next(next_panel,nowat,idx2,now.cnt+1,now.choose,now.memo+","+dirs);
		
		return next;
	
}

P finished;
int bfs(P s,int to){
    queue<P> que;
    que.push( s );
    int idx1;
    int idx2;
    P now;
    string next_panel;
    int nowDistance;
	int sum = 0;
	int min = 100000;
    while( !que.empty() ){
        now = que.front();
        que.pop();
		if( judge(now.panel) ){
			finished = now;
			cout<<"fin:"<<now.memo<<endl;
			return now.cnt;
		}
	   nowDistance = getDistance(now.space,to);
	   idx1 = now.space;
	   if(nowDistance == 0){
		sum = solver(now);
		if(sum<min&&sum!=-1){min = sum;}
		for(int i = 0;i<4;i++){
			P newP = getMovedP(now,idx1,d[i],ds[i],nowDistance,true);
			if(newP.panel==""){continue;}
			sum = solver(newP);
			if(sum<min&&sum!=-1){min = sum;}
		}
	   }
        for(int i = 0 ; i < 4 ; i++ ){
		  P newP = getMovedP(now,idx1,d[i],ds[i],nowDistance,false);
		  if(newP.panel==""){continue;}
		  que.push( newP );
		  
        }
    }
    return min;
}

int solver(P s){
	if(finished.panel!=""){return -1;}
	vector<pair<int,int> > pairs;
	int size = s.panel.size();
	int distance;
	for(int i = 0;i<size;i++){
		pair<int,int> newPair;
		distance = getDistance(i,s.panel[i]-'1');
		if(distance==0){continue;}
		newPair.first = distance;
		newPair.second = i;
		pairs.push_back(newPair);
	}
	sort(pairs.begin(),pairs.end());
	int sum = 0;
	int min = 100000;
	if(s.choose+1>chooseLimit){
		return -1;
	}
	string newMemo;
	for(int i = pairs.size()-1;i!=0;i--){
		if(pairs.size()>3&&pairs[i].first==1){continue;}
		countAll++;
		newMemo = s.memo;
		newMemo+=",choose(";
		newMemo.push_back('1'+pairs[i].second);
		newMemo+=")";
		P newP(s.panel,s.before,pairs[i].second,s.cnt,s.choose+1,newMemo);
		sum = bfs(newP,s.panel[pairs[i].second]-'1');
		
		if(sum<min&&sum!=-1){min = sum;}
	}
	return min;
}

int main(){
	clock_t old_time = clock();
    char c;
    string panel = "135246987";
	P s(panel, -1);
	int sum = solver(s);
	clock_t new_time = clock();
	cout <<"sum:"<<sum<<" n:" << countAll << " time:" <<new_time-old_time<< endl;
	cout<<finished.memo<<endl;
}

vector<vector<int> > setup(){
	vector<vector<int> > distance;
	int x,y;
	int tox,toy;
	int dis;
	for(int i = 0;i<9;i++){
		vector<int> newArray;
		for(int u = 0;u<9;u++){
			y = u/3;
			x = u%3;
			toy = i/3;
			tox = i%3;
			dis = max(y,toy)-min(y,toy)+max(x,tox)-min(x,tox);
			///cout<<"at:"<<i<<" num:"<<u+1<<" dis:"<<dis<<endl;
			newArray.push_back(dis);
		}
		distance.push_back(newArray);
	}	
	
	return distance;
}
