from django.conf.urls import patterns, url
from .views import RecommendEngine

urlpatterns = patterns('',
    url(r'^getRecommendation/', RecommendEngine.as_view())
    )
