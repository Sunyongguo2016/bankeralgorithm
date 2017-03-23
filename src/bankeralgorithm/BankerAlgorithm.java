package bankeralgorithm;


/**
 * 对银行家算法进行代码实现，模拟教材113页进行检测
 * 
 * 班级：软件4班
 * 学号：2014011725 
 * 姓名：孙永国 
 * 完成时间：2016-4-13
 * 
 * @author dell-sun
 * @version 1.1.0
 * 
 */
 class BankerAlgorithm {
	// 进程数5，资源3类，模拟教材113页
	static final int n = 5; // 进程数
	static final int m = 3; // 资源类数目

	// static int[] available = new int[m]; // 可用资源
	// static int[][] max = new int[n][m]; // 最大需求量
	// static int[][] allocation = new int[n][m]; // 已分配的资源
	// static int[][] need = new int[n][m]; // 需求矩阵
	// static int[] request = new int[m]; // 请求量

	static int[] available = { 3, 3, 2 }; // 可用资源
	static int[][] max = { { 7, 5, 3 }, 
						   { 3, 2, 2 }, 
						   { 9, 0, 2 }, 
						   { 2, 2, 2 }, 
						   { 4, 3, 3 } }; // 最大需求量
	static int[][] allocation = { { 0, 1, 0 }, 
							      { 2, 0, 0 },
							      { 3, 0, 2 }, 
							      { 2, 1, 1 }, 
							      { 0, 0, 2 } };; // 已分配的资源
	static int[][] need = { { 7, 4, 3 }, 
							{ 1, 2, 2 }, 
							{ 6, 0, 0 }, 
							{ 0, 1, 1 }, 
							{ 4, 3, 1 } };; // 需求矩阵
	static int[] request = new int[m]; // 请求量

	// 主函数 main函数
	public static void main(String[] args) {
		// 判断t0时刻的安全性 p113
		System.out.println("判断t0时刻的安全性：");
		if (securityAlgorithm()) {
			System.out.println("t0时刻是安全的");
		} else {
			System.out.println("t0时刻是不安全的");
		}

		// p1请求资源
		System.out.println("/*************************************************************/");
		request[0] = 1;
		request[1] = 0;
		request[2] = 2;
		bankerAlgorithm(request, 1);

		// p4请求资源
		System.out.println("/*************************************************************/");
		request[0] = 3;
		request[1] = 3;
		request[2] = 0;
		bankerAlgorithm(request, 4);

		// p0请求资源
		System.out.println("/*************************************************************/");
		request[0] = 0;
		request[1] = 2;
		request[2] = 0;
		bankerAlgorithm(request, 0);

		// p0请求资源
		System.out.println("/*************************************************************/");
		System.out.println("如果银行家算法中吧p0发出的向量请求改为request(0,1,0)");
		request[0] = 0;
		request[1] = 1;
		request[2] = 0;
		bankerAlgorithm(request, 0);
	}

	/**
	 * 银行家算法，用到二维矩阵加法twoMatrixAdd，二维矩阵减法twoMatrixSub，矩阵比较compare,
	 * 打印以为数组printOneMa,本类中已实现
	 * 
	 * @param request[]
	 *            请求向量
	 * @param i
	 *            进程名称,例如进程1,2,3
	 * @return
	 */
	 public static void bankerAlgorithm(int request[], int i) {
		// 步骤1
		// 打印当前即将比较的request，need，的一些信息
		System.out.println("进程" + i + "执行请求：银行家算法运行");
		System.out.println("request");
		printOneMa(request);
		System.out.println("need" + i);
		printOneMa(need[i]);

		if (compare(request, need[i])) {
			System.out.println("request<=need");
			// 步骤2
			// 打印当前即将比较的request，available，的一些信息
			System.out.println("request");
			printOneMa(request);
			System.out.println("available");
			printOneMa(available);

			if (compare(request, available)) {
				System.out.println("request<=available");
				// 步骤3
				available = oneMatrixSub(available, request);
				allocation[i] = oneMatrixAdd(allocation[i], request);
				need[i] = oneMatrixSub(need[i], request);

				// 步骤4
				// 调用安全性算法
				if (securityAlgorithm()) {
					System.out.println("此次资源分配后，系统处于安全状态，允许请求，将资源分配给" + i + "进程");
					System.out.println("此时的available：");
					printOneMa(available);
				} else {
					System.out.println("此次资源分配后，系统处于不安全状态，不允许请求，此次分配作废，资源不会分配给" + i + "进程");

					available = oneMatrixAdd(available, request);
					allocation[i] = oneMatrixSub(allocation[i], request);
					need[i] = oneMatrixAdd(need[i], request);

					System.out.println(i + "进程处于等待状态！");
					System.out.println("此时的available：");
					printOneMa(available);
				}
			} else {
				System.out.println("request>available");
				System.out.println("尚无足够资源，进程" + i + "等待 (wait)");
			}
		} else {
			System.out.println("request>need");
			System.out.println("所需要资源数，已经超出了进程" + i + "宣布的最大值（request>need）");
		}
	}

	/**
	 * 安全性算法，判断当前进程分配后系统是否处于安全状态 如果安全，返回true，否则false
	 * 
	 * @return 安全返回true
	 */
	 public static boolean securityAlgorithm() {
		// 步骤1
		// 初始化一些变量
		// int[] work = available; 错误的赋值方法，这样的话，下面程序对work进行赋值操作，改变了availabe,
		// 重新定义一个一维数组，将available的值赋给这个以为数组work
		System.out.println("-----安全性算法执行：-----");
		int[] work = new int[m];

		for (int i = 0; i < work.length; i++) {
			work[i] = available[i];
		}

		boolean[] finish = new boolean[n];
		for (int i = 0; i < finish.length; i++) {
			finish[i] = false;
		}
		int count = 0; // 计数器，用于判断是否finish[n]里所有Boolean都是true

		// 步骤2
		for (int i = 0; i < n; i++) {
			if (compare(need[i], work) && finish[i] == false) // 找到满足这个条件的进程i
			{
				// 步骤3
				System.out.print("进程" + i + "获得资源，可顺利执行，直至完成，并释放资源");
				work = oneMatrixAdd(work, allocation[i]);
				System.out.print("	 " + "Work+Allocation:");

				printOneMa(work);// 打印work+allocation

				System.out.println();
				finish[i] = true;
				// go to step2
				// 因为for循环内的部分执行后，会有i++操作，所以为了保证i从0开始，设置i=-1,i++ = 0;
				if (i == n - 1) {
					i = -1;
				}
			}
		}
		// 步骤4

		for (int j = 0; j < finish.length; j++) {
			if (finish[j] == true) {
				count++;
			}
		}

		return count == n;
	}

	/**
	 * 写一个一维数组矩阵减法函数；
	 * 
	 * @param first
	 *            一维数组
	 * @param second
	 *            一维数组
	 * @return 一维数组
	 */
	 public static int[] oneMatrixSub(int[] first, int[] second) {
		for (int i = 0; i < first.length; i++) {
			first[i] = first[i] - second[i];
		}
		return first;
	}

	/**
	 * 写一个一维数组矩阵加法函数；
	 * 
	 * @param first
	 *            一维数组
	 * @param second
	 *            一维数组
	 * @return 一维数组
	 */
	 public static int[] oneMatrixAdd(int[] first, int[] second) {
		for (int i = 0; i < first.length; i++) {
			first[i] = first[i] + second[i];
		}
		return first;
	}

	/**
	 * 写一个二维数组矩阵加法函数；
	 * 
	 * @param a
	 *            二维数组
	 * @param b
	 *            二维数组
	 * @return 二维数组
	 */
	 public int[][] twoMatrixAdd(int[][] a, int[][] b) {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				a[i][j] = a[i][j] + b[i][j];
			}
		}
		return a;
	}

	/**
	 * 写一个二维数组矩阵减法函数；
	 * 
	 * @param a
	 *            二维数组
	 * @param b
	 *            二维数组
	 * @return 二维数组
	 */
	 public static int[][] twoMatrixSub(int[][] a, int[][] b) {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				a[i][j] = a[i][j] - b[i][j];
			}
		}
		return a;
	}

	/**
	 * 矩阵比较函数，判断两个矩阵每个相应位置上的数是否都大于另一个矩阵,如果第一个小于等于第二个返回true,否则为false
	 * 
	 * @param a
	 *            一维数组
	 * @param b
	 *            一维数组
	 * @return boolean
	 */
	 public static boolean compare(int[] first, int[] second) {
		int count = 0;
		for (int i = 0; i < first.length; i++) {
			if (first[i] <= second[i]) {
				count++;
			}
		}
		
		return count == first.length;
	}

	/**
	 * 打印一维数组内容
	 * 
	 * @param temp 
	 * 				一维数组
	 */
	 public static void printOneMa(int[] temp) {
		for (int i = 0; i < temp.length; i++) {
			System.out.print(temp[i]);
			System.out.print("	");
		}
		System.out.println();
	}
	 
}
