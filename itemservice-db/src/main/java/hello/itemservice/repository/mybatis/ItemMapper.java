package hello.itemservice.repository.mybatis;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;

@Mapper
public interface ItemMapper {
	void save(Item item);

	void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateDto);

	Optional<Item> findById(Long id);

	List<Item> findAll(ItemSearchCond itemSearch);
}
