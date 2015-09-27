from rest_framework.views import APIView
from rest_framework.response import Response
from parse_rest.datatypes import Object, GeoPoint

class event(Object):
    pass

class Tags(Object):
    pass

class _User(Object):
    pass


class RecommendEngine(APIView):
    def get(self, request):
        uid = request.GET.get('uid', None)
        latitude = request.GET.get('latitude', 28.52)
        longitude = request.GET.get('longitude', 77.23)
        user_loc = GeoPoint(latitude=latitude, longitude=longitude)
        evts = event.Query.filter(location__nearSphere=user_loc).select_related('project')
        result = []
        for evt in evts:
            evt_obj = {
            'latitude':  evt.location.latitude,
            'longitude': evt.location.longitude,
            'project_name': evt.project.name
            }
            result.append(evt_obj)
        return Response(result)
