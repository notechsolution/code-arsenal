package com.ru.arsenal.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GuavaBloomFilterTest {

  public static void main(String[] args) {
    int insertions = 2000000;
    double falsePositiveProbability = 0.001;
    BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), insertions, falsePositiveProbability);
    Set<String> sets = new HashSet<String>(insertions);
    List<String> lists = new ArrayList<String>(insertions);

    for (int i = 0; i < insertions; i++) {
      String uuid = UUID.randomUUID().toString();
      bloomFilter.put(uuid);
      sets.add(uuid);
      lists.add(uuid);
    }

    int right = 0;
    int wrong = 0;

    for (int i = 0; i < 10000; i++) {
      String data = i % 100 == 0 ? lists.get(i / 100) : UUID.randomUUID().toString();
      if (bloomFilter.mightContain(data)) {
        if (sets.contains(data)) {
          right++;
          continue;
        }
        wrong++;
      }
    }

    NumberFormat percentFormat = NumberFormat.getPercentInstance();
    percentFormat.setMaximumFractionDigits(2);
    float percent = (float)wrong / 9900;
    float bingo = (float) (9900 - wrong)/ 9900;

    System.out.println("在 " + insertions + " 条数据中，判断 100 实际存在的元素，布隆过滤器认为存在的数量为：" + right);
    System.out.println("在 " + insertions + " 条数据中，判断 9900 实际不存在的元素，布隆过滤器误认为存在的数量为：" + wrong);
    System.out.println("命中率为：" + percentFormat.format(bingo) + "，误判率为：" + percentFormat.format(percent));

  }
}
