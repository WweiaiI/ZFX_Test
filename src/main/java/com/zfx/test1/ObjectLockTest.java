package com.zfx.test1;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectLockTest {

	public static final ConcurrentHashMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();
	public static AtomicInteger atomicInterger = new AtomicInteger();
	
	public static void main(String[] args) {
		String key = "" + "#" + "" + new Random().nextInt(1000);
		LOCK_MAP.put(key, new Object());//
		
//		new ThreadB(key, atomicInterger).start();//
		new ThreadA(key, atomicInterger).start();
		
//		LOCK_MAP.put(key, new Object());//
		new ThreadB(key, atomicInterger).start();
		
//		LOCK_MAP.put(key, new Object());//
		new ThreadA(key, atomicInterger).start();
	}
}

class ThreadA extends Thread {
	
	private AtomicInteger atomicInterger;
	private String key;
	
	public ThreadA(String key,AtomicInteger atomicInterger) {
		this.atomicInterger = atomicInterger;
		this.key = key;
	}

	@Override
	public void run() {
		super.run();
		if (ObjectLockTest.LOCK_MAP.get(key) != null) {
			synchronized (ObjectLockTest.LOCK_MAP.get(key)) {
				try {
					int addAndGet = atomicInterger.addAndGet(1);
					System.out.println("i"+addAndGet);
				} finally {
					ObjectLockTest.LOCK_MAP.remove(key);
				}
			}
		} else {
			String key = "" + "#" + "" + new Random().nextInt(1000);
			ObjectLockTest.LOCK_MAP.put(key, new Object());
			synchronized (ObjectLockTest.LOCK_MAP.get(key)) {
				try {
					int addAndGet = atomicInterger.addAndGet(1);
					System.out.println("iii"+addAndGet);
				} finally {
					ObjectLockTest.LOCK_MAP.remove(key);
				}
			}
		}
	}
}

class ThreadB extends Thread {
	
	private AtomicInteger atomicInterger;
	private String key;
	
	public ThreadB(String key,AtomicInteger atomicInterger) {
		this.atomicInterger = atomicInterger;
		this.key = key;
	}

	@Override
	public void run() {
		super.run();
		if (ObjectLockTest.LOCK_MAP.get(key) != null) {
			synchronized (ObjectLockTest.LOCK_MAP.get(key)) {
				while (true) {
					System.out.println("while (true)1");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			String key = "" + "#" + "" + new Random().nextInt(1000);
			ObjectLockTest.LOCK_MAP.put(key, new Object());
			synchronized (ObjectLockTest.LOCK_MAP.get(key)) {
				while (true) {
					System.out.println("while (true)2");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}