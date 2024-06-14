package growthook.org.bamgang.trail.service;

import growthook.org.bamgang.attractions.domain.Attraction;
import growthook.org.bamgang.attractions.repository.AttractionRepository;
import growthook.org.bamgang.members.domain.PickedWalk;
import growthook.org.bamgang.members.repository.DataPickedWalkRepository;
import growthook.org.bamgang.trail.domain.Safety;
import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.dto.response.GetNewTrailResponseDto;
import growthook.org.bamgang.trail.dto.response.GetSearchTrailResponseDto;
import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;
import growthook.org.bamgang.trail.repository.SafetyRepository;
import growthook.org.bamgang.trail.repository.TrailRepository;
import growthook.org.bamgang.trail.repository.TrailStartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class TrailServiceImpl implements TrailService {

    public final TrailRepository trailRepository;
    public final TrailStartRepository trailStartRepository;
    public final DataPickedWalkRepository dataPickedWalkRepository;
    public final AttractionRepository attractionRepository;
    public final SafetyRepository safetyRepository;
    public final ApiService apiService;

    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository, TrailStartRepository trailStartRepository, DataPickedWalkRepository dataPickedWalkRepository, AttractionRepository attractionRepository, SafetyRepository safetyRepository, ApiService apiService) {
        this.trailRepository = trailRepository;
        this.trailStartRepository = trailStartRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
        this.attractionRepository = attractionRepository;
        this.safetyRepository = safetyRepository;
        this.apiService = apiService;
    }

    @Override
    public GetTrailResponseDto getTrailById(Integer trailId, Integer userId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new RuntimeException("Trail not found with id: " + trailId));
        PickedWalk pickedWalk = dataPickedWalkRepository.findByUserIdAndTrailId(userId, trailId);
        boolean pick = pickedWalk != null;
        // Domain 객체를 Response DTO로 변환
        return GetTrailResponseDto.builder()
                .id(trail.getId())
                .title(trail.getTitle())
                .detail(trail.getDetail())
                .image(trail.getImage())
                .distance(trail.getDistance())
                .rating(trail.getRating())
                .time(trail.getTime())
                .level(trail.getLevel())
                .region(trail.getRegion())
                .latitudeList(trail.getLatitudeList())
                .longitudeList(trail.getLongitudeList())
                .picked(pick)
                .build();
    }

    @Override
    public List<GetTrailResponseDto> getNearTrail(Double latitude, Double longitude) {
        Double minLatitude = latitude - 0.05;
        Double maxLatitude = latitude + 0.05;
        Double minLongitude = longitude - 0.05;
        Double maxLongitude = longitude + 0.05;

        List<Integer> nearTrailStarts = trailStartRepository.findTrailIdsByCoordinates(
                minLatitude, maxLatitude, minLongitude, maxLongitude,
                minLatitude, maxLatitude, minLongitude, maxLongitude,
                minLatitude, maxLatitude, minLongitude, maxLongitude
        );

        List<Trail> nearByTrails = nearTrailStarts.stream()
                .map(trailRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        nearByTrails.sort(Comparator.comparingDouble(trail -> calculateDistance(latitude, longitude, trail.getTrailStart().getStartLatitude1(), trail.getTrailStart().getStartLongitude1())));

        return nearByTrails.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private double calculateDistance(Double userLat, Double userLon, Double trailLat, Double trailLon) {
        final int R = 6371; // 지구의 반경 (단위: km)
        double latDistance = Math.toRadians(trailLat - userLat);
        double lonDistance = Math.toRadians(trailLon - userLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(trailLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환 (단위: km)
    }

    @Override
    public List<GetTrailResponseDto> getNearAttractionTrail(Double latitude, Double longitude) {
        Double minLatitude = latitude - 0.2;
        Double maxLatitude = latitude + 0.2;
        Double minLongitude = longitude - 0.3;
        Double maxLongitude = longitude + 0.3;

        List<Integer> nearTrailStarts = trailStartRepository.findTrailIdsByCoordinates(
                minLatitude, maxLatitude, minLongitude, maxLongitude,
                minLatitude, maxLatitude, minLongitude, maxLongitude,
                minLatitude, maxLatitude, minLongitude, maxLongitude
        );

        List<Trail> nearByTrails = nearTrailStarts.stream()
                .map(trailRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return nearByTrails.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetTrailResponseDto> getPopularTrail() {
        List<Trail> popularTrails = trailRepository.findTop10ByOrderByRatingDesc();

        return popularTrails.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetNewTrailResponseDto> getNewTrail(Double latitude, Double longitude) {
        List<GetNewTrailResponseDto> responseList = new ArrayList<>();
        // 현재 위치 기반 관광지 2개 선택
        List<Attraction> attractions = attractionRepository.findNearestAttractions(latitude,longitude);
        for(int T = 0;T<5;T++) {
            Attraction atrraction1 = attractions.get(T);
            Attraction atrraction2 = attractions.get(T+5);
            // 관광지를 경유지로 활용하여 산책로 생성
            // 첫번째 경유지 좌표
            Double latitude1 = Double.parseDouble(atrraction1.getAttractionLatitude());
            Double longitude1 = Double.parseDouble(atrraction1.getAttractionLongitude());
            // 두번째 경유지 좌표
            Double latitude2 = Double.parseDouble(atrraction2.getAttractionLatitude());
            Double longitude2 = Double.parseDouble(atrraction2.getAttractionLongitude());

            // 경유지 리스트 생성
            String passList = longitude1 + "," + latitude1 + "_" + longitude2 + "," + latitude2;

            // 시작점과 도착점을 현재 위치로 설정
            String startName = "Start Point";
            String endName = "End Point";

            try {
                String routeData = apiService.getRouteData(longitude, latitude, longitude, latitude, startName, endName, passList);

                // GeoJSON 데이터 파싱
                JSONObject jsonObject = new JSONObject(routeData);
                JSONArray features = jsonObject.getJSONArray("features");

                List<Double> latitudes = new ArrayList<>();
                List<Double> longitudes = new ArrayList<>();
                double totalTime = 0;
                double totalDistance = 0;

                for (int i = 0; i < features.length(); i++) {
                    JSONObject feature = features.getJSONObject(i);
                    JSONObject geometry = feature.getJSONObject("geometry");
                    JSONObject properties = feature.getJSONObject("properties");

                    String type = geometry.getString("type");
                    if (type.equals("LineString")) {
                        JSONArray coordinates = geometry.getJSONArray("coordinates");
                        for (int j = 0; j < coordinates.length(); j++) {
                            JSONArray coord = coordinates.getJSONArray(j);
                            longitudes.add(coord.getDouble(0));
                            latitudes.add(coord.getDouble(1));
                        }

                        totalTime += properties.getDouble("time");
                        totalDistance += properties.getDouble("distance");
                    }
                }

                Double[] latitudeList = latitudes.toArray(new Double[0]);
                Double[] longitudeList = longitudes.toArray(new Double[0]);
                Double time = totalTime / 3600; // convert seconds to hours
                Double distance = totalDistance / 1000; // convert meters to kilometers

                time = Math.round(time * 100) / 100.0;
                distance = Math.round(distance * 100) / 100.0;

                // 산책로 경로 좌표들 전달
                GetNewTrailResponseDto dto = new GetNewTrailResponseDto();
                dto.setLatitudeList(latitudeList);
                dto.setLongitudeList(longitudeList);
                dto.setTime(time);
                dto.setDistance(distance);
                dto.setImage(atrraction1.getAttractionUrl());
                dto.setTitle(atrraction1.getAttractionName() +", "+atrraction2.getAttractionName()+ "산책로");
                dto.setDetail("시원한 밤공기와 함께"+atrraction1.getAttractionName() + "," +atrraction2.getAttractionName() + " 풍경을 즐겨보아요.");
                if(distance>10){
                    dto.setLevel("고급");
                }else if(distance>5){
                    dto.setLevel("중급");
                }else{
                    dto.setLevel("초급");
                }

                String region = atrraction1.getAttractionRegion().split(" ")[1];
                dto.setRegion(region);

                // 시설물 찾기
                List<Safety> nearbyFacilities = findNearbyFacilities(latitudeList, longitudeList, 50.0);
                List<Integer> safetyTypeList = new ArrayList<>();
                List<Double> safetyLatitudeList = new ArrayList<>();
                List<Double> safetyLontiudeList = new ArrayList<>();

                long cctvCount = 0;
                long lightCount = 0;
                for(Safety safety : nearbyFacilities){
                    safetyTypeList.add(Integer.parseInt(safety.getType()));
                    if(safety.getType().equals("305")){
                        lightCount++;
                    }else{
//                    System.out.println(safety.getType());
                        cctvCount++;
                    }
                    safetyLatitudeList.add(Double.parseDouble(safety.getLatitude()));
                    safetyLontiudeList.add(Double.parseDouble(safety.getLongitude()));
                }

                Integer[] safetyType = safetyTypeList.toArray(new Integer[0]);
                Double[] safetyLatitude = safetyLatitudeList.toArray(new Double[0]);
                Double[] safetyLontitude = safetyLontiudeList.toArray(new Double[0]);

                dto.setSafetyTypeList(safetyType);
                dto.setSafetyLatitudeList(safetyLatitude);
                dto.setSafetyLongitudeList(safetyLontitude);

                dto.setCctvCount(cctvCount);
                dto.setLightCount(lightCount);

                if(distance/(cctvCount+lightCount)<0.01){
                    dto.setSafetyPercent(100);
                }else if((cctvCount+lightCount)/distance<0.1){
                    dto.setSafetyPercent(80);
                }else if((cctvCount+lightCount)/distance<0.2){
                    dto.setSafetyPercent(50);
                }else if((cctvCount+lightCount)/distance<0.4){
                    dto.setSafetyPercent(30);
                }else{
                    dto.setSafetyPercent(10);
                }

                Trail testDto = new Trail();
                testDto.setTitle(dto.getTitle());
                testDto.setDetail(dto.getDetail());
                testDto.setLevel(dto.getLevel());
                testDto.setImage(dto.getImage());
                testDto.setTime(dto.getTime());
                testDto.setDistance(dto.getDistance());
                testDto.setRegion(dto.getRegion());
                testDto.setLatitudeList(dto.getLatitudeList());
                testDto.setLongitudeList(dto.getLongitudeList());
                testDto.setRating(1.0);
                int id = trailRepository.findMaxTrailId();
                dto.setId(id+1);
                dto.setRating(1.0);
                responseList.add(dto);

                trailRepository.save(testDto);
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return responseList;
            }
        }
        return responseList;
    }

    @Override
    public GetSearchTrailResponseDto getSearchTrail(Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude) {
        // 시작점과 도착점을 현재 위치로 설정
        String startName = "Start Point";
        String endName = "End Point";

        try {
            //시작점 도착점으로 TMAP API를 통해서 경로 검색
            String routeData = apiService.getRouteData(startLongitude, startLatitude, endLongitude, endLatitude, startName, endName);

            // GeoJSON 데이터 파싱
            JSONObject jsonObject = new JSONObject(routeData);
            JSONArray features = jsonObject.getJSONArray("features");

            List<Double> latitudes = new ArrayList<>();
            List<Double> longitudes = new ArrayList<>();
            double totalTime = 0;
            double totalDistance = 0;

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONObject properties = feature.getJSONObject("properties");

                String type = geometry.getString("type");
                if (type.equals("LineString")) {
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    for (int j = 0; j < coordinates.length(); j++) {
                        JSONArray coord = coordinates.getJSONArray(j);
                        longitudes.add(coord.getDouble(0));
                        latitudes.add(coord.getDouble(1));
                    }

                    totalTime += properties.getDouble("time");
                    totalDistance += properties.getDouble("distance");
                }
            }

            Double[] latitudeList = latitudes.toArray(new Double[0]);
            Double[] longitudeList = longitudes.toArray(new Double[0]);
            Double time = totalTime / 3600; // convert seconds to hours
            Double distance = totalDistance / 1000; // convert meters to kilometers

            time = Math.round(time * 100) / 100.0;
            distance = Math.round(distance * 100) / 100.0;

            // 시설물 찾기
            List<Safety> nearbyFacilities = findNearbyFacilities(latitudeList, longitudeList, 100.0);
            List<Integer> safetyTypeList = new ArrayList<>();
            List<Double> safetyLatitudeList = new ArrayList<>();
            List<Double> safetyLontiudeList = new ArrayList<>();

            long cctvCount = 0;
            long lightCount = 0;
            for(Safety safety : nearbyFacilities){
                safetyTypeList.add(Integer.parseInt(safety.getType()));
                if(safety.getType().equals("305")){
                    lightCount++;
                }else{
//                    System.out.println(safety.getType());
                    cctvCount++;
                }
                safetyLatitudeList.add(Double.parseDouble(safety.getLatitude()));
                safetyLontiudeList.add(Double.parseDouble(safety.getLongitude()));
            }

            Integer[] safetyType = safetyTypeList.toArray(new Integer[0]);
            Double[] safetyLatitude = safetyLatitudeList.toArray(new Double[0]);
            Double[] safetyLontitude = safetyLontiudeList.toArray(new Double[0]);

            int safetyPercent = 0;

            if(distance/(cctvCount+lightCount)<0.01){
                safetyPercent=100;
            }else if((cctvCount+lightCount)/distance<0.1){
                safetyPercent=80;
            }else if((cctvCount+lightCount)/distance<0.2){
                safetyPercent=50;
            }else if((cctvCount+lightCount)/distance<0.4){
                safetyPercent=30;
            }else{
                safetyPercent=10;
            }

            return GetSearchTrailResponseDto.builder()
                    .latitudeList(latitudeList)
                    .longitudeList(longitudeList)
                    .time(time)
                    .distance(distance)
                    .safetyLongitudeList(safetyLontitude)
                    .safetyLatitudeList(safetyLatitude)
                    .safetyTypeList(safetyType)
                    .cctvCount(cctvCount)
                    .lightCount(lightCount)
                    .safetyPercent(safetyPercent)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return new GetSearchTrailResponseDto();
        }
    }

    // 안전 시설물 검색 함수
    private List<Safety> findNearbyFacilities(Double[] latitudeList, Double[] longitudeList, double radiusMeters) {
        List<Safety> nearbyFacilities = new ArrayList<>();

        List<Safety> allFacilities = safetyRepository.findAll(); // DB에서 모든 시설물 조회

        A: for (Safety facility : allFacilities) {
            String facilityLatStr = facility.getLatitude();
            String facilityLonStr = facility.getLongitude();
            for (int i = 0; i < latitudeList.length; i++) {
                Double lat = latitudeList[i];
                Double lon = longitudeList[i];

                if (facilityLatStr != null && facilityLonStr != null) {
                    Double facilityLat = Double.parseDouble(facilityLatStr);
                    Double facilityLon = Double.parseDouble(facilityLonStr);

                    if (distance(lat, lon, facilityLat, facilityLon) <= radiusMeters) {
                        nearbyFacilities.add(facility);
                        continue A;
                    }
                }
            }
        }

        return nearbyFacilities;
    }

    //거릭 계산 함수
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (단위: km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // 단위: meter

        return distance;
    }

    @Override
    public GetTrailResponseDto convertToResponseDto(Trail trail) {
        return GetTrailResponseDto.builder()
                .id(trail.getId())
                .title(trail.getTitle())
                .detail(trail.getDetail())
                .image(trail.getImage())
                .distance(trail.getDistance())
                .rating(trail.getRating())
                .time(trail.getTime())
                .level(trail.getLevel())
                .region(trail.getRegion())
                .latitudeList(trail.getLatitudeList())
                .longitudeList(trail.getLongitudeList())
                .build();
    }
}
