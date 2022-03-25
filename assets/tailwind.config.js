module.exports = {
    mode: 'jit',
    content: [
        './js/**/*.js',
        '../lib/*_web/**/*.*ex',
        '../lib/*_web/**/*.*sface'
    ],
    theme: {
        extend: {
            animation: {
                'spin-slow': 'spin 3s linear infinite',
                wiggle: 'wiggle 1s ease-in-out infinite',
                blink: 'blink 1s steps(2, jump-none) -0.5s infinite'
            },
            keyframes: {
                wiggle: {
                    '0%, 100%': { transform: 'rotate(-3deg)' },
                    '50%': { transform: 'rotate(3deg)' },
                },
                blink: {
                    '0%': { visibility: 'visible' },
                    '50%': { visibility: 'hidden' },
                }
            }
        }
    },
    plugins: [
        require('@tailwindcss/forms')
    ],
}