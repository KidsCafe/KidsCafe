# select c.id,
         #        c.name,
          #        c.size,
          #        avg(re.star) as star,
#        count(re.id) as count,
#        case when count(ro.id) > 0 then true else false end as roomExist,
#        c.parking,
#        c.restaurant,
#        c.opened_at,
#        c.closed_at
# from cafe as c left join review as re on c.id = re.cafe_id
#      left join room as ro on c.id = ro.cafe_id
#      left join fee as f on c.id = f.cafe_id
# where star >= 2.0 and star < 3.0
# group by c.id
# order by star desc;

select count(id)
from room
group by cafe_id;

select *
from cafe
where id = 81;
# 목, 일, 수, 토
select c.id,
       c.name,
       c.address,
       c.size,
       avg(re.star),
       COUNT(DISTINCT re.id),
       case
           when (COUNT(DISTINCT ro.id) >= 1)
               then abs(sign(1))
           else false
           end,
       c.parking,
       c.restaurant,
       c.opened_at,
       c.closed_at
from cafe c
         left join review re on re.cafe_id = c.id
         left join room ro on ro.cafe_id = c.id
         left join fee f on f.cafe_id = c.id
where c.size <= 500000
  and c.parking = false
  and (
    c.day_off like '%일%' escape '!'
        and c.opened_at >= '00:00'
        and c.opened_at <= '23:59'
    )
  and c.restaurant = true
  and c.multi_family = false
  and c.opened_at >= '00:00'
  and c.opened_at <= '01:30'
group by c.id
having avg(re.star) between 0.0 and 5.0
order by avg(re.star);
# limit ?, ?

select c.id,
       c.name,
       c.address,
       c.size,
       avg(re.star),
       count(distinct re.id),
       case
           when (count(distinct ro.cafe_id) >= 1)
               then abs(sign(1))
           else 0
           end,
       c.parking,
       c.restaurant,
       c.opened_at,
       c.closed_at
from cafe c
         left join review re on re.cafe_id = c.id
         left join room ro on ro.cafe_id = c.id
         left join fee f on f.cafe_id = c.id
where c.size <= 500000
  and c.parking = false
  and c.restaurant = true
  and c.multi_family = false
  and c.opened_at >= '00:00'
  and c.closed_at <= '23:59'
group by c.id
having avg(re.star) between 0.0 and 5.0
order by avg(re.star)