package com.kcwl.common.logging.layout;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.kcwl.ddd.infrastructure.config.ISensitiveMaskConfig;
import com.kcwl.framework.utils.KcBeanRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ckwl
 */
@Slf4j
public class SensitiveMaskPatternLayout extends TraceIdPatternLogbackLayout {

    private Pattern maskPatternGroup;

    public SensitiveMaskPatternLayout(){
        maskPatternGroup = buildMaskPattern();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String message = super.doLayout(event);
        if ( !isEnableMaskField() ) {
            return message;
        }
        if ( maskPatternGroup == null ) {
            maskPatternGroup = buildMaskPattern();
            if ( maskPatternGroup == null ) {
                return message;
            }
        }
        // 处理日志信息
        try {
            return process(message);
        } catch (Exception e) {
            // 这里不做任何操作,直接返回原来message
            log.error("{}", e.getMessage());
            return message;
        }
    }

    /**
     * 替换信息
     *
     * @param message
     * @return
     */
    public String process(String message) {
        StringBuilder sb = new StringBuilder(message);
        Matcher matcher = maskPatternGroup.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
                if (matcher.group(group) != null) {
                    IntStream.range(matcher.start(group),
                        matcher.end(group)).forEach(i -> sb.setCharAt(i, '*'));
                }
            });
        }
        return sb.toString();
    }

    private boolean isEnableMaskField(){
        KcBeanRepository kcBeanRepository = KcBeanRepository.getInstance();
        ISensitiveMaskConfig sensitiveMaskConfig = kcBeanRepository.getBean("sensitiveMaskConfig", ISensitiveMaskConfig.class);
        if ( sensitiveMaskConfig != null ) {
            return sensitiveMaskConfig.isEnableMaskField();
        }
        return false;
    }

    private Pattern buildMaskPattern(){
        KcBeanRepository kcBeanRepository = KcBeanRepository.getInstance();
        ISensitiveMaskConfig sensitiveMaskConfig = kcBeanRepository.getBean("sensitiveMaskConfig", ISensitiveMaskConfig.class);
        if ( (sensitiveMaskConfig != null) && (sensitiveMaskConfig.getMaskFieldList() !=null ) ) {
            Set<String> maskFields = sensitiveMaskConfig.getMaskFieldList();
            List<String> maskPatterns = new ArrayList<>(maskFields.size());
            for (String maskField : maskFields) {
                maskPatterns.add("\"" + maskField + "\":\\[?\"([^\"]*)");
                maskPatterns.add(maskField + "=([^,|}]*)");
            }
            return Pattern.compile(maskPatterns.stream().collect(Collectors.joining("|")), Pattern.MULTILINE);
        }
        return null;
    }
}
