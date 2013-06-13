import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class bankerall {

 
	public static void main(String[] args) throws FileNotFoundException {
		File AllocationFile = new File("1.txt");//已讀檔的方式進行Allocation 的檔案
		File MaxFile = new File("2.txt");//已讀檔的方式進行MAX 的檔案
		File AvailableFile = new File("3.txt");//已讀檔的方式進行Available 的檔案
		Scanner inputallocation = new Scanner(AllocationFile);// 
		Scanner inputmax = new Scanner(MaxFile);// 
		Scanner inputavailable = new Scanner(AvailableFile);// 
		int number = 0;// 
		int column = 0;//  
		int row = 0;//  
		while (inputavailable.hasNext()) {
			number = inputavailable.nextInt();// 
			column++;// 
		}
		while (inputallocation.hasNext()) {
			number = inputallocation.nextInt();//開始讀allocation
			row++;// 計數器 技術
		}
		inputallocation.close();//關閉資源
		inputavailable.close();//關閉資源
		int Allocation[][] = new int[row / column][column];//c2/c1可以知道 有幾個process 宣告allocation存放陣列
		int Max[][] = new int[row / column][column];// 宣存放陣列MAx
		int Available[] = new int[column];//available 存放陣列
		int Need[][] = new int[row / column][column];//宣存放陣列need
		int AllocationTemp[][] = new int[row / column][column];//宣存放陣列第二次使用的allocation
		inputallocation = new Scanner(AllocationFile);//重新開啟掃描器
		inputmax = new Scanner(MaxFile);//重新開啟掃描器
		inputavailable = new Scanner(AvailableFile);//重新開啟掃描器
		int count1 = 0, count2 = 0;//控制2微陣列的位置
		while (inputallocation.hasNext()) {//開始讀檔輸入
			Allocation[count1][count2] = inputallocation.nextInt();//把數字填入allocation陣列
			count2++;//填完column
			if (count2 % column == 0) {//判斷跳行
				count2 = 0;//把 column規0
				count1++;//row 增加
			}
		}
		 count1 = 0; count2 = 0;//控制2微陣列的位置
		while (inputmax.hasNext()) {
			Max[count1][count2] = inputmax.nextInt();//把數字填入max陣列
			count2++;//填完column
			if (count2 % column == 0) {
				count1++;//row 增加
				count2 = 0;//把 column規0
			}
		}
		  count1 = 0;//控制陣列的位置
		while (inputavailable.hasNext()) {//開始讀檔
			Available[count1] = inputavailable.nextInt();//將數字填入avail陣列
			count1++;//陣列位置增加
		}
		for (int i = 0; i < (row / column); i++) {
			for (int j = 0; j < (count1); j++) {//由兩個count相除可以看出process個數
				Need[i][j] = (Max[i][j] - Allocation[i][j]);//用max-allocation可以得到need
			}
		}
		for (int i = 0; i < (row / column); i++) {
			for (int j = 0; j < (count1); j++) {
				AllocationTemp[i][j] =Allocation[i][j];//設一個暫存的allocation 陣列 儲存
			}
		}
		int totaltimes = ((row / column) * ((row / column) - 1)) / 2;//計算出此演算法最多會執行(n*n-1)/2次
		
		int work[] = new int[column];//設一個work陣列 長度與available相同
		for (int i = 0; i < work.length; i++) {//使用一個迴圈跑 available 陣列長度的次數
			work[i] = Available[i];//初始work 把available的值放入
		}
		int checkpoint = 0;
		int num = 0;
		int ProcessFinish[] = new int[(row / column)];//宣告一陣列來判斷是否該地process 已被執行
		boolean safe = true;//布林值為判斷的標準
		while (checkpoint < totaltimes) {
			if (checkneedandwork(work, Need, num) == true) {//檢查是否need小於等於work
				//workadd(work, TAllo, num);//以上符合 開始增加work
				for (int i = 0; i < work.length; i++) {
					work[i] += AllocationTemp[num][i];//把allocation累加到work中
					AllocationTemp[num][i] = 0;//加完後allocation歸零
				}
				//cccheck(ProcessFinish, num);//記錄哪個process已被執行
				ProcessFinish[num] = 1;
			}
			checkpoint++;//執行次數
			num++;//陣列的位置
			if (num >= (row / column)) {
				num = 0;
			}
		}
		for (int i = 0; i < ProcessFinish.length; i++) {
			if (ProcessFinish[i] == 0) {//如果判斷式的位置為零代表不被執行
				safe = false;//就判定為unsafe
				break;//結束判斷迴圈
			}
		}
		System.out.println("(1)執行safety Algorithm:");//因出字樣
		if (safe == true) {//如果判斷式為成立的  那麼演算法為安全
			System.out.println("safe");
			
		//part2
			System.out.println("(2)執行Resource-Request Algorithm:");
			Scanner in  = new Scanner(System.in);//設立一個掃描器讓使用者輸入
			System.out.print("請輸入欲提出request的process:");
			int processsnumber = in.nextInt();//讀入使用者輸入的process代號
			System.out.print("請輸入欲提出request的值:");
			int R[] = new int[work.length];//此為裝置request值得陣列
			for (int i = 0; i < R.length; i++) {
				R[i] = in.nextInt();//讓使用者輸入request值
			}
			int Workagain[] = new int[work.length];
			int Final[] = new int[(row / column)];
			checkpoint = 0;
			num = 0;
			if (Checkneed(R, Need, processsnumber) == true) {//檢查第一步驟是否成立
				if (Checkavailable(R, Available) == true) {//檢查第二步驟是否成立
					//Cavail(Available, R);
					for (int i = 0; i < Available.length; i++) {
						Available[i] -= R[i];//將available 扣掉request的值
						R[i] = Available[i];
					}
					 for (int i = 0; i < R.length; i++) {
						Allocation[processsnumber][i] += R[i];//allocation 要加上request
						Need[processsnumber][i] -= R[i];//need扣掉request
					}
				 
						while (checkpoint < totaltimes) {
							if (checkneedandwork(work, Need, num) == true) {//檢查是否need小於等於work
								//workadd(work, TAllo, num);//以上符合 開始增加work
								for (int i = 0; i < work.length; i++) {
									work[i] += AllocationTemp[num][i];//把allocation累加到work中
									AllocationTemp[num][i] = 0;//加完後allocation歸零
								}
								//cccheck(ProcessFinish, num);//記錄哪個process已被執行
								ProcessFinish[num] = 1;
							}
							checkpoint++;//執行次數
							num++;//陣列的位置
							if (num >= (row / column)) {
								num = 0;
							}
						}
						for (int i = 0; i < Final.length; i++) {
							if (Final[i] == 0) {//如果判斷式的位置為零代表不被執行
								safe = false;//就判定為unsafe
								break;//結束判斷迴圈
							}
						}
						if (safe == true) {
							System.out.println("safe"); 
						} else {
							System.out.println("unsafe"); 
						}


				} else {
					System.out.print("error Request >Available");//印出第二步驟錯誤字樣
				}
			} else {
				System.out.print("error  Request >Need");//印出第一步驟錯誤字樣
			}
		
		
		
		}
	 else {
		System.out.println("unsafe");//如果判斷式為非  演算法為不安全的
	}
		
	}

	private static boolean Checkavailable(int[] r, int[] available) {
		int correct = 0;
		for (int i = 0; i < r.length; i++) {
			if (r[i] <= available[i]) {//檢查request值有沒有比available小
				correct++;//往下檢查
			}
		}
		return (correct == r.length);//回傳是否為真
	}

	private static boolean Checkneed(int[] r, int[][] need, int processsnumber) {
		int correct = 0;
		for (int i = 0; i < r.length; i++) {
			if (r[i] <= need[processsnumber][i]) {//檢查每個requese值有沒有比need小
				correct++;//往下檢查
			}
		}
		return (correct == r.length);
	}

	private static boolean checkneedandwork(int[] work, int[][] need, int num) {
		int checksum = 0;
		for (int i = 0; i < work.length; i++) {
			if (work[i] >= need[num][i]) {//檢查work中的每個值是否都比need大
				checksum++;//往下檢查
			}
		}
		return (checksum == work.length);//回傳是否為真
		 
	}

}
