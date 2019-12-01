package dataContainers;
//package com.code.revisited.concurrent.locks;
 
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
 
/**
 * This class demonstrates ReadWriteLock that was introduced in Java 5.0
 * @author sureshsajja
 *
 */
public class MyConcurrentHashMap<K,V> {
 
	private Map<K,V> hashMap = new HashMap<>();
	//non-fair read write lock
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	public MyConcurrentHashMap(){
		
	}
	
	public void put(K key, V value){
		writeLock.lock();
		try{
			hashMap.put(key, value);
		}finally{
			writeLock.unlock();
		}
	}
	
	public V get(K key){
		readLock.lock();
		try{
			return hashMap.get(key);
		}finally{
			readLock.unlock();
		}
	}
	
	public V remove(K key){
		writeLock.lock();
		try{
			return hashMap.remove(key);
		}finally{
			writeLock.unlock();
		}
	}
	
	public boolean containsKey(K key){
		readLock.lock();
		try{
		return hashMap.containsKey(key);
		}finally{
			readLock.unlock();
		}
	}
	
	public static void main(String[] args)
	 {
		MyConcurrentHashMap<Integer,String> myCHM = new MyConcurrentHashMap<Integer,String>();
		Runnable r1 = new Runnable() {
			  @Override 
			  public void run() {	
				  for (int i=0;i<1000;i++) {
					 int index = (int)Math.round(Math.random()*500); 
			         myCHM.put(index, "name"+index );
				     System.out.println("put: name" + index);
			         try{
			        	 Thread.sleep((long)(Math.random()*500));
			         } catch (Exception e)
			         {};
			        
				  }
			    
			  }
		};
		
		Runnable r2 = new Runnable() {
			  @Override 
			  public void run() {
				  
			      String name;
				  for (int i=0;i<1000;i++) {
					 int index = (int)Math.round(Math.random()*500); 
			         name = myCHM.get(index);
			         if (name != null)
			        	 System.out.println("get: " + name);
			         try{
			        	 Thread.sleep((long)(Math.random()*500));
			         } catch (Exception e)
			         {};
				  }
			  }
		};
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		
		t1.start();
		t2.start();
		
	 }
 
}