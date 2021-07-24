package com.veluxer.occupying.application

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SrtPropertiesTest(srtProperties: SrtProperties) : StringSpec({
    "host 속성은 코레일 호스트 주소를 반환한다" {
        srtProperties.host shouldBe "https://app.srail.or.kr"
    }

    "timeout 속성은 코레일 API의 타임아웃 설정값을 반환한다." {
        srtProperties.timeout shouldBe 2000L
    }
})
