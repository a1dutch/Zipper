echo "Pulling Changes"
git pull

echo "`nPulling Tags"
git pull --tags

echo "`nComputing last relased version for tags"
$tag=git describe --abbrev=0 --tags
$tag = $tag.Substring(7)

echo $tag

echo "`nPerforming site update for version $tag"
mvn "-Dsite-version=$tag" clean site-deploy -f update-site.pom
