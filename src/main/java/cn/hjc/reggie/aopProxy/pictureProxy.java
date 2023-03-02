package cn.hjc.reggie.aopProxy;

import cn.hjc.reggie.dto.DishDto;
import cn.hjc.reggie.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Aspect
@Component
public class pictureProxy {

    @Autowired
    private DishMapper dishMapper;

    @Value("${file.path}")
    private String basePath;

    /**
     * 菜品更新切面
     */
    @Pointcut("execution(boolean cn.hjc..DishService.update(..))")
    public void dishUpdate() {
    }

    /**
     * 菜品删除切面
     */
    @Pointcut("execution(boolean cn.hjc..DishService.del(..))")
    public void dishDelete() {
    }

    /**
     * 同步删除菜品更新后失效的图片
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("dishUpdate()")
    public Object pictureUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        DishDto dishDto = (DishDto) args[0];
        String imgName = dishMapper.getById(dishDto.getId()).getImage();
        Object proceed = joinPoint.proceed();
        String newImgName = dishMapper.getById(dishDto.getId()).getImage();
        if (!imgName.equals(newImgName)) {
            try {
                File file = new File(basePath + imgName);
                file.delete();
                log.info("菜品图片已删除 ---> " + imgName);
            } catch (Exception e) {
                log.error("菜品图片删除失败 ---> " + imgName);
            }
        }

        return proceed;
    }

    @Around("dishDelete()")
    public Object pictureDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long[] ids = (Long[]) args[0];

        String[] imgNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            imgNames[i] = dishMapper.getById(ids[i]).getImage();
        }

        Object proceed = joinPoint.proceed();

        for (int i = 0; i < imgNames.length; i++) {
            try {
                File file = new File(basePath + imgNames[i]);
                file.delete();
                log.info("菜品图片已删除 ---> " + imgNames[i]);
            } catch (Exception e) {
                log.error("菜品图片删除失败 ---> " + imgNames[i]);
            }
        }
        return proceed;
    }

}
