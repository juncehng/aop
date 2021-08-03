package com.liu.aop.cfg.circleDependces;

import com.liu.aop.aspect.annotation.SysOperaLog;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Spring对需要被AOP代理增强的Bean的创建流程分为两种：
 * 1、单个Bean要被AOP增强
 *   -->首先使用正常的创建Bean流程创建Bean
 *   ----->AbstractAutowireCapableBeanFactory.doCreateBean()创建Bean
 *
 * 2、循环依赖中的Bean需要被代理增强
 *
 *
 * 两个重要的Lambada表达式：
 * 这两个表达式都是【ObjectFactory】接口的实现类
 * 第一个：这个接口此时的实现类的getObject方法为调用createBean(beanName, mbd, args)方法，启动Bean的创建流程
 * sharedInstance = getSingleton(beanName, () -> {
 * 	try {
 * 		return createBean(beanName, mbd, args);
 *  }catch (BeansException ex) {
 * 		// Explicitly remove instance from singleton cache: It might have been put there
 * 		// eagerly by the creation process, to allow for circular reference resolution.
 * 		// Also remove any beans that received a temporary reference to the bean.
 * 		destroySingleton(beanName);
 * 		throw ex;
 *    }
 * });
 *
 * 第二个：这个Lambada是在doCreateBean这个方法里面的，将当前被创建的Bean添加到第三级缓存中（此时Bean实例已经被创建，但是还未populate）
 * 所有的bean单实例创建都会添加到三级缓存中，但是执行三级缓存需要触发条件，只要达到了触发条件才会执行，否则就算添加进去了也不会执行
 * 这个接口被触发的条件是：这个对象存在循环依赖。
 * 解决循环依赖的关键：从三级缓存中获取bean，并放到二级缓存中，最后再从二级缓存中获取初始换完成的Bean，并放到一级缓存中
 * addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
 *
 * protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
 *     // 将当前正在创建的Bean赋值给exposedObject
 *     // 若不需要被AOP增强，则直接返回
 *     // 若需要被AOP增强，
 *     Object exposedObject = bean;
 *     if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
 *         for (BeanPostProcessor bp : getBeanPostProcessors()) {
 *             if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
 *                 SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
 *                 // 如果此时对象需要被AOP增强，则创建并返回代理对象
 *                 exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
 *             }
 *         }
 *     }
 *     return exposedObject;
 * }
 *
 *
 * ###Spring解决循环依赖的核心方法
 * protected Object getSingleton(String beanName, boolean allowEarlyReference) {
 *     // 尝试从一级缓存中获取Bean，这个Bean已经是一个创建完成的Bean了
 *     Object singletonObject = this.singletonObjects.get(beanName);
 *     if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
 *         synchronized (this.singletonObjects) {
 *             // 尝试在二级缓存中获取被AOP增强后的Bean，这时Bean是通过在三级缓存中缓存的【ObjectFactory】
 *             // 创建好的代理对象
 *             singletonObject = this.earlySingletonObjects.get(beanName);
 *             if (singletonObject == null && allowEarlyReference) {
 *                 // 获取第三级缓存中保存的ObjectFactory（上面的第二个Lambada表达式）
 *                 ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
 *                 // 第二三级缓存被触发的条件是：这个对象存在循环依赖
 *                 if (singletonFactory != null) {
 *                     // 尝试使用第三级缓存中缓存的【ObjectFactory】，获取Bean
 *                     // 1、若当前对象不需要被AOP增强，则直接返回当前正在创建的对象
 *                     // 2、若当前对象需要被增强，则通过调用后置处理器创建代理对象，并将代理对象赋值给正在创建的Bean并返回
 *                     singletonObject = singletonFactory.getObject();
 *                     this.earlySingletonObjects.put(beanName, singletonObject);
 *                     this.singletonFactories.remove(beanName);
 *                 }
 *             }
 *         }
 *     }
 *     return singletonObject;
 * }
 *
 *
 *
 *
 * 循环依赖测试--AOP代理总结
 * 1、CircleA和CircleB循环依赖，CircleA又属于一个要被AOP增强的Bean
 * 2、如果新创建CircleA，在CircleA创建过程中需要注入CircleB（此时A在第三级缓存中），则此时程序就会去创建CircleB
 *    这时CircleA只是创建了实例还没有为属性赋值。
 * 3、流程继续创建CircleB实例，在创建B实例的实例的时候发现需要依赖注入A，则此时程序调用doCreateBean去尝试创建实例A
 *    doCreateBean会首先去调用getSingleton(beanName)方法，这个方法会从第三级缓存中获取到CircleA并放到二级缓存中
 *
 *
 *
 *
 * @author: Jack.Cheng
 * @date: 2021/3/9 10:47
 */
@Service
public class CircleA implements InitializingBean {

    @Autowired
    private CircleB circleb;

    @SysOperaLog(desc = "循环依赖时，代理对象测试")
    public void proxyCircleTestMethod(){

    }

    @PostConstruct
    public void PostConstructMethod(){
        System.out.println("CircleA PostConstructMethod...");
    }

    public CircleA() {
        System.out.println("CircleA construct...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("CircleA afterPropertiesSet...");
    }

    public void testMethodA(){
        System.out.println("调用methodA的方法：" + circleb.testMethod());
    }
}
