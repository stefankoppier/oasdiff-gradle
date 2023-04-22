module.exports = {
    branchPrefix: 'renovate/',
    username: 'renovate-release',
    gitAuthor: 'Renovate Bot <bot@renovateapp.com>',
    onboarding: true,
    forkProcessing: 'disabled',
    platform: 'github',
    globalExtends: ["config:base", "group:all", ":disableDependencyDashboard"]
    repositories: ['stefankoppier/oasdiff-gradle']
}