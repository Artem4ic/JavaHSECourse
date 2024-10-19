class Complex {
    private final double real;
    private final double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() {
        return real;
    }

    public double modulusSquared() {
        return real * real + imag * imag;
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.real - other.real, this.imag - other.imag);
    }

    public Complex multiply(Complex other) {
        return new Complex(
                this.real * other.real - this.imag * other.imag,
                this.real * other.imag + this.imag * other.real
        );
    }

    public Complex divide(Complex other) {
        double denominator = other.real * other.real + other.imag * other.imag;
        if (denominator == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return new Complex(
                (this.real * other.real + this.imag * other.imag) / denominator,
                (this.imag * other.real - this.real * other.imag) / denominator
        );
    }

    @Override
    public String toString() {
        return String.format("(%.2f + %.2fi)", real, imag);
    }
}
